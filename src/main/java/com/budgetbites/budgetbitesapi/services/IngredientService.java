package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.fasterxml.jackson.databind.JsonNode;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IngredientService {
    
    Ingredient getIngredientById(Long id);

    void fetchIngredients() throws IOException, InterruptedException, SchedulerException;

    List<Ingredient> getMatchingIngredients(String title);

    JsonNode getResponseFromWebsite(int fetchedResultsCount) throws IOException, InterruptedException;

    void updateOrCreate(Ingredient ingredient);

    void updateIngredientsValidity(Date date) throws SchedulerException;

    boolean validateIngredientList(List<Long> ingredientIds);

    Date getDate();
}
