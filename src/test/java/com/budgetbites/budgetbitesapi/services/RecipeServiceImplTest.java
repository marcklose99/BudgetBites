package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.exceptions.RecipeNotFoundException;
import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.models.Instruction;
import com.budgetbites.budgetbitesapi.models.Recipe;
import com.budgetbites.budgetbitesapi.models.RecipeDTO;
import com.budgetbites.budgetbitesapi.repository.InstructionRepository;
import com.budgetbites.budgetbitesapi.repository.RecipeIngredientRepository;
import com.budgetbites.budgetbitesapi.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    @Mock
    private RecipeRepository mockedRecipeRepository;
    private  S3BucketStorageService mockedS3BucketStorageService;

    private  InstructionRepository mockedInstructionRepository;

    private  RecipeIngredientRepository mockedRecipeIngredientRepository;

    private RecipeServiceImpl recipeService;

    @BeforeEach
    public void setup() {
        mockedRecipeRepository = mock(RecipeRepository.class);
        mockedInstructionRepository = mock(InstructionRepository.class);
        mockedRecipeIngredientRepository = mock(RecipeIngredientRepository.class);
        mockedRecipeRepository = mock(RecipeRepository.class);
        recipeService = new RecipeServiceImpl(mockedRecipeRepository, mockedS3BucketStorageService,mockedInstructionRepository, mockedRecipeIngredientRepository);

    }

    @Test
    public void getRecipeById_RecipeExists_ReturnsRecipeDTO() {

        Instruction instruction1 = new Instruction();
        instruction1.setText("abc");
        instruction1.setStepId(1);
        Recipe recipe = new Recipe(
                "Rindergulasch",
                "",
                "imageName",
                List.of(instruction1),
                Set.of(new Ingredient(1L,
                        "Tomato",
                        new BigDecimal("1.0"),
                        "Kaufland",
                        "Kaufland",
                        LocalDateTime.of(2023, LocalDate.now().plusMonths(1).getMonthValue(), LocalDate.now().plusDays(1).getDayOfMonth(), 12, 0),
                        LocalDateTime.of(2023, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth(), 1, 0),
                        false,
                        "",
                        0
                )));

        recipe.setId(1L);


        when(mockedRecipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));

        ResponseEntity<RecipeDTO> response = recipeService.getRecipeById(recipe.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(recipe.getId(), response.getBody().getId());
        verify(mockedRecipeRepository, times(1)).findById(recipe.getId());


    }
    @Test
    public void getRecipeById_RecipeDoesNotExist_ThrowsRecipeNotFoundException() {
        Long recipeId = 1L;

        when(mockedRecipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.getRecipeById(recipeId);
        });

        verify(mockedRecipeRepository, times(1)).findById(recipeId);
    }
}
