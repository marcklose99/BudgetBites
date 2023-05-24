package com.budgetbites.budgetbitesapi.repository;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredient, Long> {
    @Query(value = "SELECT * FROM ingredient i WHERE i.title LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Ingredient> findByTitle(String title);
}
