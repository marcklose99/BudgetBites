package com.budgetbites.budgetbitesapi.exceptions;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(long recipe_id) {
        super(String.format("Recipe not found with id: %s", recipe_id));
    }
}
