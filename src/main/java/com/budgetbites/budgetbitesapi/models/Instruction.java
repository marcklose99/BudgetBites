package com.budgetbites.budgetbitesapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int stepId;

    @Column(nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @JsonIgnore
    private Recipe recipe;
}
