package com.budgetbites.budgetbitesapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
public class RecipeDTO {


    private Long id;

    private String title;

    private Set<Ingredient> ingredientList;


    private List<Instruction> instructionList;


    private String imageName;

    private String description;
}
