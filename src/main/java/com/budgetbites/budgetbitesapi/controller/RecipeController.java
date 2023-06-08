package com.budgetbites.budgetbitesapi.controller;

import com.budgetbites.budgetbitesapi.models.Recipe;
import com.budgetbites.budgetbitesapi.services.RecipeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
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
        if(recipe.getId() == null) {
            return recipeService.createRecipe(recipe);
        } else {
            return recipeService.updateRecipe(recipe.getId(), recipe);
        }

    }
    @DeleteMapping("/recipes/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }
}
