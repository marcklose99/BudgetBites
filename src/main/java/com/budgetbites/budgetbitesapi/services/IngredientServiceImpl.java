package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.exceptions.IngredientNotFoundException;
import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.repository.IngredientRepository;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Implementation of the IngredientService.
 */
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final SchedulerService schedulerService;
    private final FetchService fetchService;

    private Date date;

    /**
     * Retrieves a list of ingredients matching the given title.
     *
     * @param title the title to match.
     * @return a list of matching ingredients.
     */
    @Override
    public List<Ingredient> getMatchingIngredients(String title) {
        return ingredientRepository.findByTitle(title);
    }

    @Override
    public List<Ingredient> getMatchingIngredients(String title, String filter) {
        return ingredientRepository.findByTitle(title, filter);
    }

    /**
     * Retrieves an ingredient by its ID.
     *
     * @param id the ID of the ingredient.
     * @return the ingredient with the specified ID.
     * @throws IngredientNotFoundException if the ingredient is not found.
     */
    @Override
    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    /**
     * A known ingredient gets updated otherwise it's created
     */
    @Override
    public void create(int postalCode) {
        this.date = new Date();
        List<Ingredient> ingredients = fetchService
                .getIngredients(postalCode)
                .stream()
                .map(ingredient -> {
                    if(isIngredientValid(ingredient)) {
                        ingredient.setValid(true);
                        return ingredient;
                    }
                    return ingredient;
                })
                .toList();

        ingredientRepository.saveAll(ingredients);
    }

    /**
     * Updates the validity of ingredients for the specified date and schedules the job.
     *
     * @param date the date to update the validity for.
     * @throws SchedulerException if an error occurs while scheduling the job.
     */
    @Override
    public void updateIngredientsValidity(Date date) throws SchedulerException {
        List<Ingredient> ingredientsToUpdate = ingredientRepository.findByDate(date);
        ingredientsToUpdate.forEach(ingredient -> ingredient.setValid(false));
        ingredientRepository.saveAll(ingredientsToUpdate);
        scheduleJob();
    }

    /**
     * Schedules the job for updating ingredient execution date.
     *
     * @throws SchedulerException if an error occurs while scheduling the job.
     */
    public void scheduleJob() throws SchedulerException {
        schedulerService.updateJobExecutionDate(getDate());
    }

    /**
     * Checks if an ingredient is valid based on its validFrom and validTo dates.
     *
     * @param ingredient the ingredient to check.
     * @return true if the ingredient is valid, false otherwise.
     */
    private boolean isIngredientValid(Ingredient ingredient) {
        Date validFrom = ingredient.getValidFrom();
        Date validTo = ingredient.getValidTo();
        return date.after(validFrom) && date.before(validTo);
    }

    /**
     * Retrieves the smallest date from all ingredients.
     *
     * @return the date.
     */
    public Date getDate() {
        return ingredientRepository.findMinDate().getValidTo();
    }

    @Override
    public List<Ingredient> findAllById(List<Long> ingredientIds) {
        return ingredientRepository.findAllById(ingredientIds);
    }

    /**
     * Validates a list of ingredient IDs.
     *
     * @param ingredientIds the list of ingredient IDs to validate.
     * @return true if the ingredient IDs are valid, false otherwise.
     */
    @Override
    public boolean validateIngredientList(List<Long> ingredientIds) {
        try {
            ingredientRepository.findAllById(ingredientIds);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
        return true;
    }
}
