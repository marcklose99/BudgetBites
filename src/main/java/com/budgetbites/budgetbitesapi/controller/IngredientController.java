package com.budgetbites.budgetbitesapi.controller;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.services.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/ingredients/fetch/{postalCode}")
    public void fetchIngredients(@PathVariable int postalCode) {
        ingredientService.create(postalCode);
    }


    @GetMapping({"/ingredients/{title}", "/ingredients/{title}/{filter}"})
    public ResponseEntity<List<Ingredient>> getMatchingIngredients(@PathVariable String title,
                                                                   @PathVariable(required = false) String filter) {
        if (!title.isEmpty() && title.length() > 1) {
            if(filter != null) {
                List<Ingredient> matchingIngredients = ingredientService.getMatchingIngredients(title, filter.toLowerCase());
                return new ResponseEntity<>(matchingIngredients, HttpStatus.OK);
            } else {
                List<Ingredient> matchingIngredients = ingredientService.getMatchingIngredients(title);
                return new ResponseEntity<>(matchingIngredients, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
