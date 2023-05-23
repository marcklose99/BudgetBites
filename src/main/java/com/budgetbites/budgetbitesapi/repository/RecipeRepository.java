package com.budgetbites.budgetbitesapi.repository;

import com.budgetbites.budgetbitesapi.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}