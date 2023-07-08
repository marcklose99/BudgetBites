package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IngredientServiceImplTest {

    private IngredientService ingredientService;
    private FetchService mockedFetchService;
    private SchedulerService mockedSchedulerService;

    IngredientRepository mockedIngredientRepository;

    @BeforeEach
    void setUp() {
        mockedIngredientRepository = mock(IngredientRepository.class);
        mockedSchedulerService = mock(SchedulerService.class);
        mockedFetchService = mock(FetchService.class);
        ingredientService = new IngredientServiceImpl(mockedIngredientRepository, mockedSchedulerService, mockedFetchService);
    }

    @Test
    void create_ShouldReturnListWithIngredientsAndCorrectValidity() throws SchedulerException {
        // Given
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1L,
                "Tomato",
                new BigDecimal("1.0"),
                "Kaufland",
                "Kaufland",
                LocalDateTime.of(2023, LocalDate.now().plusMonths(1).getMonthValue(), LocalDate.now().plusDays(1).getDayOfMonth(), 12, 0),
                LocalDateTime.of(2023, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth(), 1, 0),
                false,
                "",
                0
        ));
        ingredients.add(new Ingredient(1L,
                "Cheese",
                new BigDecimal("1.0"),
                "Kaufland",
                "Kaufland",
                LocalDateTime.of(2300, 1, 1,12, 0),
                LocalDateTime.of(2023, LocalDate.now().plusMonths(1).getMonthValue(), LocalDate.now().plusDays(1).getDayOfMonth(),12, 0),
                false,
                "",
                0

        ));

        when(mockedFetchService.getIngredients(12459)).thenReturn(ingredients);
        when(mockedIngredientRepository.findMinDate()).thenReturn(ingredients.get(0));

        // When
        List<Ingredient> actualIngredients = ingredientService.create(12459);

        // Then
        assertAll(
                () -> assertEquals(actualIngredients.size(), ingredients.size()),
                () -> assertTrue(actualIngredients.get(0).isValid()),
                () -> assertFalse(actualIngredients.get(1).isValid())
        );

    }

}
