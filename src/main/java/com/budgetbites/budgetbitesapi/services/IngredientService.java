package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public interface IngredientService {
    
    Ingredient getIngredientById(Long id);
    List<Ingredient> fetchIngredients() throws IOException, InterruptedException;

    List<Ingredient> getMatchingIngredients(String title);

    JsonNode getResponseFromWebsite(int fetchedResultsCount) throws IOException, InterruptedException;

    void createIngredient(Ingredient ingredient);
}
