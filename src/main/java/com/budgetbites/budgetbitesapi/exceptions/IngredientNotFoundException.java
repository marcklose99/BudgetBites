package com.budgetbites.budgetbitesapi.exceptions;

public class IngredientNotFoundException extends RuntimeException {

    public IngredientNotFoundException(long id) {
        super(String.format("Ingredient not found with id: %s", id));
    }
}
