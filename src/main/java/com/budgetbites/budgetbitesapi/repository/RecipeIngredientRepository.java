package com.budgetbites.budgetbitesapi.repository;

import com.budgetbites.budgetbitesapi.models.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
}
