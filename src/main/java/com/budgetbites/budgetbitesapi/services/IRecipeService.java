package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Recipe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IRecipeService {

    ResponseEntity<List<Recipe>> getAllRecipes(String filter);

    ResponseEntity<Recipe> getRecipeById(Long id);

    ResponseEntity<Recipe> createRecipe(Recipe recipe, MultipartFile file);

    ResponseEntity<Recipe> updateRecipe(Long id, Recipe recipe);

    void deleteRecipe(Long id);
}
