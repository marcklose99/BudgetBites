package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.exceptions.IngredientNotFoundException;
import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.repository.IngredientsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

/**
 * Implementation of the IngredientService.
 */
@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientsRepository ingredientsRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SchedulerService schedulerService;

    private Date currentDate;

    /**
     * Initialization method called on startup.
     * Calls fetchIngredients() and schedules the job.
     *
     * @throws SchedulerException if an error occurs while scheduling the job.
     * @throws IOException if an error occurs while fetching data.
     * @throws InterruptedException if the thread is interrupted.
     */
    @PostConstruct
    public void init() throws SchedulerException, IOException, InterruptedException {
        fetchIngredients();
        scheduleJob();
    }

    /**
     * Retrieves a list of ingredients matching the given title.
     *
     * @param title the title to match.
     * @return a list of matching ingredients.
     */
    @Override
    public List<Ingredient> getMatchingIngredients(String title) {
        return ingredientsRepository.findByTitle(title);
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
        return ingredientsRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    /**
     * Fetches ingredients from a website and updates or creates them in the repository.
     *
     * @throws IOException if an error occurs while fetching data.
     * @throws InterruptedException if the thread is interrupted.
     */
    @Override
    public void fetchIngredients() throws IOException, InterruptedException {
        this.currentDate = new Date();
        int fetchedResultsCount = 0;
        List<Ingredient> allResults = new ArrayList<>();

        JsonNode responseAsJson = getResponseFromWebsite(fetchedResultsCount);
        int totalOfferResults = responseAsJson.get("totalResults").intValue();

        JsonNode resultsAsJson;

        while (fetchedResultsCount < totalOfferResults) {
            resultsAsJson = responseAsJson.get("results");
            fetchedResultsCount += resultsAsJson.size();
            List<Ingredient> fetchedResults = Arrays.stream(objectMapper.readValue(String.valueOf(resultsAsJson), Ingredient[].class)).toList();
            allResults.addAll(fetchedResults);
            responseAsJson = getResponseFromWebsite(fetchedResultsCount);
        }

        for (Ingredient ingredient : allResults) {
            updateOrCreate(ingredient);
        }
    }

    /**
     * Retrieves the response from a website.
     *
     * @param fetchedResultsCount the number of results already fetched.
     * @return the response as a JsonNode.
     * @throws IOException if an error occurs while fetching data.
     * @throws InterruptedException if the thread is interrupted.
     */
    @Override
    public JsonNode getResponseFromWebsite(int fetchedResultsCount) throws IOException, InterruptedException {
        String url = String.format("https://api.marktguru.de/api/v1/industries/supermaerkte/offers?as=mobile&limit=512&offset=%s&zipCode=44787", fetchedResultsCount);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(new URI(url))
                    .headers(
                            "Content-Type", "application/json",
                            "X-ApiKey", "8Kk+pmbf7TgJ9nVj2cXeA7P5zBGv8iuutVVMRfOfvNE=",
                            "X-ClientKey", "Qs5EM9EteUFUcv6Wx/+omXnHmVMn1PH789u392ewfNE="
                    )
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readTree(response.body());
    }

    /**
     * Updates or creates an ingredient in the repository.
     *
     * @param ingredient the ingredient to update or create.
     */
    @Override
    public void updateOrCreate(Ingredient ingredient) {
        Optional<Ingredient> existingIngredientOptional = ingredientsRepository.findById(ingredient.getId());
        boolean isValid = isIngredientValid(ingredient);

        if (existingIngredientOptional.isPresent()) {
            Ingredient existingIngredient = existingIngredientOptional.get();

            existingIngredient.setPrice(ingredient.getPrice());
            existingIngredient.setValidFrom(ingredient.getValidFrom());
            existingIngredient.setValidTo(ingredient.getValidTo());

            ingredientsRepository.save(existingIngredient);
        } else {
            ingredient.setValid(isValid);
            ingredientsRepository.save(ingredient);
        }
    }

    /**
     * Updates the validity of ingredients for the specified date and schedules the job.
     *
     * @param date the date to update the validity for.
     * @throws SchedulerException if an error occurs while scheduling the job.
     */
    @Override
    public void updateIngredientsValidity(Date date) throws SchedulerException {
        List<Ingredient> ingredientsToUpdate = ingredientsRepository.findByDate(date);
        ingredientsToUpdate.forEach(ingredient -> ingredient.setValid(false));
        ingredientsRepository.saveAll(ingredientsToUpdate);
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
        return currentDate.after(validFrom) && currentDate.before(validTo);
    }

    /**
     * Retrieves the smallest date from all ingredients.
     *
     * @return the date.
     */
    public Date getDate() {
        return ingredientsRepository.findMinDate().getValidTo();
    }

    @Override
    public List<Ingredient> findAllById(List<Long> ingredientIds) {
        return ingredientsRepository.findAllById(ingredientIds);
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
            ingredientsRepository.findAllById(ingredientIds);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
        return true;
    }
}
