package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Recipe;
import com.budgetbites.budgetbitesapi.models.RecipeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IRecipeService {

    ResponseEntity<List<Recipe>> getAllRecipes();

    ResponseEntity<Recipe> getRecipeById(Long id);

    ResponseEntity<Recipe> createRecipe(RecipeDTO recipe, MultipartFile file);

    ResponseEntity<Recipe> updateRecipe(Long id, Recipe recipe);

    void deleteRecipe(Long id);
}
