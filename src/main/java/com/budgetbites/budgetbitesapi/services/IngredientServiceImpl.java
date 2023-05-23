package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.exceptions.IngredientNotFoundException;
import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.repository.IngredientsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientsRepository ingredientsRepository;

    @Override
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientsRepository.findAll();
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Ingredient> getIngredientById(Long id) {
        Ingredient ingredient = ingredientsRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
        return new ResponseEntity<>(ingredient, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Ingredient> createIngredient(Ingredient ingredient) {
        try {
            ingredientsRepository.save(ingredient);
            return new ResponseEntity<>(ingredient, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
