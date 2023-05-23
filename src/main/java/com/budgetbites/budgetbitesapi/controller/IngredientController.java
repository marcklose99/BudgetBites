package com.budgetbites.budgetbitesapi.controller;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.services.IngredientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientServiceImpl ingredientService;

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> getSingleIngredient(@PathVariable Long id) {
        return ingredientService.getIngredientById(id);
    }

    @PostMapping(
            value = "/ingredients",
            consumes = "application/json")
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.createIngredient(ingredient);
    }
}
