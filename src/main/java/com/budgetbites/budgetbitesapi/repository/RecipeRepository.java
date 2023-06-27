package com.budgetbites.budgetbitesapi.repository;

import com.budgetbites.budgetbitesapi.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {


    @Query(value = "    SELECT r.*\n" +
            "    FROM recipe r\n" +
            "    INNER JOIN recipe_ingredient ri ON r.recipe_id = ri.recipe_id\n" +
            "    INNER JOIN ingredient i ON ri.ingredient_id = i.ingredient_id\n" +
            "    GROUP BY r.recipe_id\n" +
            "    HAVING COUNT(DISTINCT CASE WHEN i.is_Valid = 1 THEN i.name_Of_Retailer END) >= (COUNT(DISTINCT ri.ingredient_id) / 2)", nativeQuery = true)
    List<Recipe> recipesOnOffer();

    @Query(value = "SELECT r.* \n" +
            "FROM recipe r\n" +
            "INNER JOIN recipe_ingredient ri ON r.recipe_id = ri.recipe_id\n" +
            "INNER JOIN Ingredient i ON ri.ingredient_id = i.ingredient_id\n" +
            "GROUP BY r.recipe_id\n" +
            "HAVING COUNT(DISTINCT i.name_of_retailer) <= :marketCount", nativeQuery = true)
    List<Recipe> recipesByMarketCount(int marketCount);
}