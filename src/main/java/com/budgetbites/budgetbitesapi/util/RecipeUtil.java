package com.budgetbites.budgetbitesapi.util;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.models.Recipe;
import com.budgetbites.budgetbitesapi.models.RecipeDTO;
import com.budgetbites.budgetbitesapi.models.RecipeIngredient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeUtil {

    public static List<RecipeDTO> mapToDTO(List<Recipe> recipes) {
        return recipes.stream().map(recipe -> {
            Set<Ingredient> ingredients = new HashSet<>();
            Set<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();

            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                Ingredient ingredient = recipeIngredient.getIngredient();
                ingredient.setCount(recipeIngredient.getCount());
                ingredients.add(ingredient);
            }
            return new RecipeDTO(recipe.getId(), recipe.getTitle(), ingredients, recipe.getInstructionList(), recipe.getImageName(), recipe.getDescription());
        }).toList();
    }

    public static RecipeDTO mapToDTO(Recipe recipe) {

        Set<Ingredient> ingredients = new HashSet<>();
        Set<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();

        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            Ingredient ingredient = recipeIngredient.getIngredient();
            ingredient.setCount(recipeIngredient.getCount());
            ingredients.add(ingredient);
        }
        return new RecipeDTO(recipe.getId(), recipe.getTitle(), ingredients, recipe.getInstructionList(), recipe.getImageName(), recipe.getDescription());
    }

    public static Set<RecipeIngredient> getRecipeIngredients(Recipe recipe, Set<Ingredient> ingredients) {
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();
        if (recipe.getId() != null && recipe.getId() != 0) {
            Set<Ingredient> existingIngredient = recipe.getRecipeIngredients().stream().map(RecipeIngredient::getIngredient).collect(Collectors.toSet());
            for (Ingredient ingredient : ingredients) {
                if (!existingIngredient.contains(ingredient)) {
                    RecipeIngredient recipeIngredient = new RecipeIngredient(ingredient, ingredient.getCount());
                    recipeIngredient.setRecipe(recipe);
                    recipeIngredients.add(recipeIngredient);
                    ingredient.setCount(0);
                }

            }
            recipeIngredients.addAll(recipe.getRecipeIngredients());
        } else {
            for (Ingredient ingredient : ingredients) {
                RecipeIngredient recipeIngredient = new RecipeIngredient(ingredient, ingredient.getCount());
                recipeIngredient.setRecipe(recipe);
                recipeIngredients.add(recipeIngredient);
                ingredient.setCount(0);
            }
        }
        return recipeIngredients;
    }
}
