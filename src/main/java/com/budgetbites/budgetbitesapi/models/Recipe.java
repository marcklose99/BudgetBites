package com.budgetbites.budgetbitesapi.models;

import com.budgetbites.budgetbitesapi.util.RecipeUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;


    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Set<RecipeIngredient> recipeIngredients;

    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<Instruction> instructionList;

    @Column(nullable = false)
    private String imageName;

    @Lob
    @Column(name="TEXT", length=512)
    private String description;

    public Recipe(String title,
                  String description,
                  String imageName,
                  List<Instruction> instructionList,
                  Set<Ingredient> ingredients) {

        this.title = title;
        for(Instruction instruction : instructionList) {
            instruction.setRecipe(this);
        }
        this.description = description;
        this.imageName = imageName;
        this.instructionList = instructionList;
        this.recipeIngredients = RecipeUtil.getRecipeIngredients(this, ingredients);
    }

    public Recipe() {

    }
}
