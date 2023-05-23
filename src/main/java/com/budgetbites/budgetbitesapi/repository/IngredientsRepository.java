package com.budgetbites.budgetbitesapi.repository;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredient, Long> {
}
