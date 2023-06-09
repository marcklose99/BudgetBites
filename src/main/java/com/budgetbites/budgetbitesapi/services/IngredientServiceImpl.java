package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the IngredientService.
 */
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final SchedulerService schedulerService;
    private final FetchService fetchService;

    private LocalDateTime dateTime;


    @Override
    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id).get();
    }

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
     * A known ingredient gets updated otherwise it's created
     */
    @Override
    public List<Ingredient> create(int postalCode) throws SchedulerException {
        this.dateTime = LocalDateTime.now();
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
        scheduleJob();
        return ingredients;
    }

    /**
     * Updates the validity of ingredients for the specified date and schedules the job.
     *
     * @param date the date to update the validity for.
     * @throws SchedulerException if an error occurs while scheduling the job.
     */
    @Override
    public void updateIngredientsValidity(LocalDateTime date) throws SchedulerException {
        System.out.println("Updated ingredients");
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
        schedulerService.updateJobExecutionDate(getNextExpireDate());
    }

    /**
     * Checks if an ingredient is valid based on its validFrom and validTo dates.
     *
     * @param ingredient the ingredient to check.
     * @return true if the ingredient is valid, false otherwise.
     */
    private boolean isIngredientValid(Ingredient ingredient) {
        LocalDateTime validFrom = ingredient.getValidFrom();
        LocalDateTime validTo = ingredient.getValidTo();
        boolean isValid = dateTime.isAfter(validFrom) && dateTime.isBefore(validTo);
        return isValid;
    }

    /**
     * Retrieves the smallest date from all ingredients.
     *
     * @return the date.
     */
    public LocalDateTime getNextExpireDate() {
        return ingredientRepository.findMinDate().getValidTo();
    }

    @Override
    public List<Ingredient> findAllById(List<Long> ingredientIds) {
        return ingredientRepository.findAllById(ingredientIds);
    }

    @Override
    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id).get();
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
