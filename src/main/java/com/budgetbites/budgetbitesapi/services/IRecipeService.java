package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Recipe;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRecipeService {

    ResponseEntity<List<Recipe>> getAllRecipes();

    ResponseEntity<Recipe> getRecipeById(Long id);

    ResponseEntity<Recipe> createRecipe(Recipe recipe);

    ResponseEntity<Recipe> updateRecipe(Long id, Recipe recipe);

    ResponseEntity<Recipe> deleteRecipe(Long id);
}