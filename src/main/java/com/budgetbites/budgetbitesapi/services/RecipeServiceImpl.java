package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.exceptions.RecipeNotFoundException;
import com.budgetbites.budgetbitesapi.models.*;
import com.budgetbites.budgetbitesapi.repository.InstructionRepository;
import com.budgetbites.budgetbitesapi.repository.RecipeIngredientRepository;
import com.budgetbites.budgetbitesapi.repository.RecipeRepository;
import com.budgetbites.budgetbitesapi.util.RecipeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements IRecipeService {

    private final RecipeRepository recipeRepository;
    private final S3BucketStorageService s3BucketStorageService;

    private final InstructionRepository instructionRepository;

    private final RecipeIngredientRepository recipeIngredientRepository;

    /**
     * Retrieves all recipes.
     *
     * @return ResponseEntity containing the list of recipes
     */
    @Override
    @Transactional
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(String filter) {
        List<Recipe> recipes = new ArrayList<>();
        if (filter.equals("all")) {
            recipes = recipeRepository.findAll();
        } else if (filter.equals("offer")) {
            recipes = recipeRepository.recipesOnOffer();
        } else {
            recipes = recipeRepository.recipesByMarketCount(Integer.parseInt(filter));
        }
        return new ResponseEntity<>(RecipeUtil.mapToDTO(recipes), HttpStatus.OK);
    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe to retrieve
     * @return ResponseEntity containing the retrieved recipe
     * @throws RecipeNotFoundException if the recipe with the given ID is not found
     */
    @Override
    public ResponseEntity<RecipeDTO> getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
        return new ResponseEntity<>(RecipeUtil.mapToDTO(recipe), HttpStatus.OK);
    }

    /**
     * Creates a new recipe.
     *
     * @param recipe the recipe to create
     * @return ResponseEntity containing the created recipe
     */
    @Override
    public ResponseEntity<RecipeDTO> createRecipe(Recipe recipe,
                                               MultipartFile file,
                                               Set<Ingredient> ingredients) {
        String uniqueFileName = s3BucketStorageService.generateFileName(file);
        s3BucketStorageService.uploadFile(uniqueFileName, file);
        Recipe savedRecipe = recipeRepository.save(new Recipe(
                recipe.getTitle(),
                recipe.getDescription(),
                uniqueFileName,
                recipe.getInstructionList(),
                ingredients
                ));
        return new ResponseEntity<>(RecipeUtil.mapToDTO(savedRecipe), HttpStatus.CREATED);
    }

    /**
     * Updates an existing recipe.
     *
     * @param id            the ID of the recipe to update
     * @param updatedRecipe the updated recipe information
     * @throws RecipeNotFoundException if the recipe with the given ID is not found
     */
    @Override
    public ResponseEntity<RecipeDTO> updateRecipe(Long id,
                                                  Recipe updatedRecipe,
                                                  Set<Ingredient> ingredients) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
        updatedRecipe.setId(id);
        updatedRecipe.setImageName(recipe.getImageName());
        remove(recipe, ingredients);
        updatedRecipe.setRecipeIngredients(RecipeUtil.getRecipeIngredients(recipe, ingredients));


        List<Instruction> instructionsRemove = new ArrayList<>();
        for(Instruction instruction : recipe.getInstructionList()) {
            for(Instruction instruction1 : updatedRecipe.getInstructionList()) {
                if(!Objects.equals(instruction1.getId(), instruction.getId())) {
                    if(!ingredients.contains(instruction))
                    instructionsRemove.add(instructionRepository.findById(instruction.getId()).get());
                }
            }
        }
        for(Instruction instruction : instructionsRemove) {
            instruction.setRecipe(null);
            instructionRepository.deleteById(instruction.getId());
            instructionRepository.save(instruction);
        }
        for(Instruction instruction : updatedRecipe.getInstructionList()) {
            instruction.setRecipe(updatedRecipe);
        }
        recipe.setInstructionList(updatedRecipe.getInstructionList());

        recipeRepository.save(updatedRecipe);
        return new ResponseEntity<>(RecipeUtil.mapToDTO(updatedRecipe), HttpStatus.CREATED);
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param id the ID of the recipe to delete
     * @throws RecipeNotFoundException if the recipe with the given ID is not found
     */

    private void remove(Recipe recipe, Set<Ingredient> ingredients) {
        Set<Ingredient> existingIngredient = recipe.getRecipeIngredients().stream().map(RecipeIngredient::getIngredient).collect(Collectors.toSet());
        Set<Ingredient> remove = existingIngredient.stream().filter(ingredient1 -> !ingredients.contains(ingredient1)).collect(Collectors.toSet());
        if (!remove.isEmpty()) {
            Set<RecipeIngredient> recipeIngredientsToRemove = recipe.getRecipeIngredients().stream()
                    .filter(recipeIngredient -> remove.contains(recipeIngredient.getIngredient()))
                    .collect(Collectors.toSet());


            recipe.getRecipeIngredients().removeAll(recipeIngredientsToRemove);
            recipeIngredientRepository.deleteAll(recipeIngredientsToRemove);
        }
    }
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
