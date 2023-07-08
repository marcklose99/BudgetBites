package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import org.quartz.SchedulerException;

import java.time.LocalDateTime;
import java.util.List;

public interface IngredientService {
    
    Ingredient getIngredientById(Long id);

    List<Ingredient> getMatchingIngredients(String title);
    List<Ingredient> getMatchingIngredients(String title, String filter);

    List<Ingredient> create(int postalCode) throws SchedulerException;

    void updateIngredientsValidity(LocalDateTime date) throws SchedulerException;

    boolean validateIngredientList(List<Long> ingredientIds);

    LocalDateTime getNextExpireDate();

    List<Ingredient> findAllById(List<Long> ingredientIds);

    Ingredient findById(Long id);
}
