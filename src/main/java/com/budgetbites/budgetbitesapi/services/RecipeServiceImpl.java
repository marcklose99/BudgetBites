package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.exceptions.RecipeCreationException;
import com.budgetbites.budgetbitesapi.exceptions.RecipeNotFoundException;
import com.budgetbites.budgetbitesapi.models.Recipe;
import com.budgetbites.budgetbitesapi.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements IRecipeService {

    private final RecipeRepository recipeRepository;

    /**
     * Retrieves all recipes.
     *
     * @return ResponseEntity containing the list of recipes
     */
    @Override
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
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
     * @throws RecipeCreationException if an error occurs while creating the recipe
     */
    @Override
    public ResponseEntity<Recipe> createRecipe(Recipe recipe) throws RecipeCreationException {
        try {
            recipeRepository.save(recipe);
            return new ResponseEntity<>(recipe, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RecipeCreationException();
        }
    }

    /**
     * Updates an existing recipe.
     *
     * @param id            the ID of the recipe to update
     * @param updatedRecipe the updated recipe information
     * @throws RecipeNotFoundException if the recipe with the given ID is not found
     */
    @Override
    public void updateRecipe(Long id, Recipe updatedRecipe) {
        recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setTitle(updatedRecipe.getTitle());
                    recipe.setIngredientList(updatedRecipe.getIngredientList());
                    return recipeRepository.save(recipe);
                })
                .orElseThrow(() -> new RecipeNotFoundException(id));
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
}
