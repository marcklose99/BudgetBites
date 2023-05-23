package com.budgetbites.budgetbitesapi.controller;

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
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        return recipeService.createRecipe(recipe);
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        return recipeService.updateRecipe(id, recipe);
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Recipe> deleteRecipe(@PathVariable Long id) {
        return recipeService.deleteRecipe(id);
    }
}
