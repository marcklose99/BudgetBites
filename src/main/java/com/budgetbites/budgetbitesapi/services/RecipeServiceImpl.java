package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.exceptions.RecipeNotFoundException;
import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.models.Instruction;
import com.budgetbites.budgetbitesapi.models.Recipe;
import com.budgetbites.budgetbitesapi.repository.InstructionRepository;
import com.budgetbites.budgetbitesapi.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements IRecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;
    private final InstructionRepository instructionRepository;
    private final S3BucketStorageService s3BucketStorageService;

    /**
     * Retrieves all recipes.
     *
     * @return ResponseEntity containing the list of recipes
     */
    @Override
    public ResponseEntity<List<Recipe>> getAllRecipes(String filter) {
        List<Recipe> recipes = new ArrayList<>();
        if (filter.equals("all")) {
            recipes = recipeRepository.findAll();
        } else if (filter.equals("offer")) {
            recipes = recipeRepository.recipesOnOffer();
        } else {
            recipes = recipeRepository.recipesByMarketCount(Integer.parseInt(filter));
        }

        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe to retrieve
     * @return ResponseEntity containing the retrieved recipe
     * @throws RecipeNotFoundException if the recipe with the given ID is not found
     */
    @Override
    public ResponseEntity<Recipe> getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    /**
     * Creates a new recipe.
     *
     * @param recipe the recipe to create
     * @return ResponseEntity containing the created recipe
     */
    @Override
    public ResponseEntity<Recipe> createRecipe(@RequestPart Recipe recipe, @RequestPart MultipartFile file) {
        Recipe createdRecipe = new Recipe();
        String uniqueFileName = s3BucketStorageService.generateFileName(file);
        s3BucketStorageService.uploadFile(uniqueFileName, file);
        List<Ingredient> ingredientList = recipe.getIngredientList();
        List<Long> ingredientIds = ingredientList.stream().map(Ingredient::getId).toList();
        if (ingredientService.validateIngredientList(ingredientIds)) {
            createdRecipe.setTitle(recipe.getTitle());
            createdRecipe.setInstructionList(recipe.getInstructionList());
            createdRecipe.setIngredientList(ingredientService.findAllById(ingredientIds));
            createdRecipe.setImageName(uniqueFileName);
            Recipe savedRecipe = recipeRepository.save(createdRecipe);
            for (Instruction instruction : savedRecipe.getInstructionList()) {
                instruction.setRecipe(savedRecipe);
            }

            instructionRepository.saveAll(savedRecipe.getInstructionList());
        } else {
            throw new IllegalArgumentException("At least one submitted id is not valid.");
        }
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    /**
     * Updates an existing recipe.
     *
     * @param id            the ID of the recipe to update
     * @param updatedRecipe the updated recipe information
     * @throws RecipeNotFoundException if the recipe with the given ID is not found
     */
    @Override
    public ResponseEntity<Recipe> updateRecipe(Long id, Recipe updatedRecipe) {
        return new ResponseEntity<>(recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setTitle(updatedRecipe.getTitle());
                    recipe.setIngredientList(updatedRecipe.getIngredientList());
                    return recipeRepository.save(recipe);
                })
                .orElseThrow(() -> new RecipeNotFoundException(id)), HttpStatus.CREATED);
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param id the ID of the recipe to delete
     * @throws RecipeNotFoundException if the recipe with the given ID is not found
     */
    @Override
    public void deleteRecipe(Long id) {
        try {
            recipeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecipeNotFoundException(id);
        }

    }

    public String getUrl(Long id) {
        Recipe recipe = recipeRepository.findById(id).get();
        return s3BucketStorageService.download(recipe.getImageName());
    }
}
