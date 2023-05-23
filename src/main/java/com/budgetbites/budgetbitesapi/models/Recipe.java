package com.budgetbites.budgetbitesapi.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Recipe {

    @Id
    @Column(name = "recipe_id", updatable = false, nullable = false)
    private Long id;

    private String title;

    @ManyToMany
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredientList;
}
