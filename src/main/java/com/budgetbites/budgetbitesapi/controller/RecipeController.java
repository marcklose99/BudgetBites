package com.budgetbites.budgetbitesapi.controller;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.models.Recipe;
import com.budgetbites.budgetbitesapi.models.RecipeDTO;
import com.budgetbites.budgetbitesapi.services.RecipeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeServiceImpl recipeService;

    @GetMapping("/recipes/overview/{filter}")
    public ResponseEntity<List<RecipeDTO>> getAllRecipes(@PathVariable(required = false) String filter) {
        return recipeService.getAllRecipes(filter);
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeDTO> getSingleRecipe(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping(path = "/recipes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createRecipe(@RequestPart Recipe recipe,
                             @RequestPart(required = false) MultipartFile file,
                             @RequestPart(required = false) Set<Ingredient> ingredients) {
        if (recipe.getId() == null || recipe.getId() == 0) recipeService.createRecipe(recipe, file, ingredients);
        else recipeService.updateRecipe(recipe.getId(), recipe, ingredients);

    }

    @DeleteMapping("/recipes/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }

    @GetMapping("/recipes/{id}/image")
    public String getUrl(@PathVariable Long id) {
        return recipeService.getUrl(id);
    }
}
