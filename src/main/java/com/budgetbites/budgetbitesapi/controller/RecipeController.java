package com.budgetbites.budgetbitesapi.controller;

import com.budgetbites.budgetbitesapi.exceptions.RecipeCreationException;
import com.budgetbites.budgetbitesapi.models.Recipe;
import com.budgetbites.budgetbitesapi.services.RecipeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeServiceImpl recipeService;

    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getSingleRecipe(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping(
            value = "/recipes",
            consumes = "application/json")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) throws RecipeCreationException {
        return recipeService.createRecipe(recipe);
    }

    @PutMapping("/recipes/{id}")
    public void updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        recipeService.updateRecipe(id, recipe);
    }

    @DeleteMapping("/recipes/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }
}
