package com.budgetbites.budgetbitesapi.repository;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredient, Long> {
    @Query(value = "SELECT * FROM ingredient i WHERE i.title LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Ingredient> findByTitle(String title);

    @Query(value = "SELECT * FROM ingredient i WHERE i.valid_to LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Ingredient> findByDate(Date executionDate);

    @Query(value = "SELECT * FROM ingredient WHERE is_valid = true AND valid_to = (SELECT MIN(valid_to) FROM ingredient WHERE is_valid = true) LIMIT 1", nativeQuery = true)
    Ingredient findMinDate();
}
