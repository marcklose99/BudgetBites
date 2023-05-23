package com.budgetbites.budgetbitesapi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
public class Ingredient {

    @Id
    @Column(name = "ingredient_id")
    private Long id;

    private String title;

    private BigDecimal price;

    @Column(name = "valid_to")
    private Date validTo;

    @Column(name = "valid_from")
    private Date validFrom;

    @ManyToMany(mappedBy = "ingredientList")
    private List<Recipe> recipes;
}
