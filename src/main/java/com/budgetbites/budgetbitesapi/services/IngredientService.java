package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import org.quartz.SchedulerException;

import java.util.Date;
import java.util.List;

public interface IngredientService {
    
    Ingredient getIngredientById(Long id);

    List<Ingredient> getMatchingIngredients(String title);
    List<Ingredient> getMatchingIngredients(String title, String filter);

    void create(int postalCode) throws SchedulerException;

    void updateIngredientsValidity(Date date) throws SchedulerException;

    boolean validateIngredientList(List<Long> ingredientIds);

    Date getNextExpireDate();

    List<Ingredient> findAllById(List<Long> ingredientIds);
}
