package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IngredientService {
    ResponseEntity<List<Ingredient>> getAllIngredients();

    ResponseEntity<Ingredient> getIngredientById(Long id);

    ResponseEntity<Ingredient> createIngredient(Ingredient ingredient);

}
