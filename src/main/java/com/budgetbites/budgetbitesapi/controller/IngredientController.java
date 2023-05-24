package com.budgetbites.budgetbitesapi.controller;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.services.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/ingredients/{title}")
    public ResponseEntity<List<Ingredient>> getMatchingIngredients(@PathVariable String title) {
        List<Ingredient> matchingIngredients = ingredientService.getMatchingIngredients(title);
        return new ResponseEntity<>(matchingIngredients, HttpStatus.OK);
    }
}
