package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface FetcherService {
    List<Ingredient> getIngredients(int postalCode);

    JsonNode fetch(int offset);
}
