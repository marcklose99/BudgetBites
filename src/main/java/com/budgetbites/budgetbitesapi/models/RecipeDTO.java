package com.budgetbites.budgetbitesapi.models;

import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {
    private String title;
    private List<Ingredient> ingredientList;
    private List<Instruction> instructionList;

}
