package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.models.Recipe;
import com.budgetbites.budgetbitesapi.models.RecipeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface IRecipeService {

    ResponseEntity<List<RecipeDTO>> getAllRecipes(String filter);

    ResponseEntity<RecipeDTO> getRecipeById(Long id);

    ResponseEntity<RecipeDTO> createRecipe(Recipe recipe, MultipartFile file, Set<Ingredient> ingredients);

    ResponseEntity<RecipeDTO> updateRecipe(Long id, Recipe recipe, Set<Ingredient> ingredients);

    void deleteRecipe(Long id);
}
