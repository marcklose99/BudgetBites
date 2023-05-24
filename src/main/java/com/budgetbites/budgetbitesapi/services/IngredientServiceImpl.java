package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.exceptions.IngredientNotFoundException;
import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.budgetbites.budgetbitesapi.repository.IngredientsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientsRepository ingredientsRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Ingredient> getMatchingIngredients(String title) {
        return ingredientsRepository.findByTitle(title);
    }

    @Override
    public Ingredient getIngredientById(Long id) {
        return ingredientsRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    @Override
    public List<Ingredient> fetchIngredients() throws IOException, InterruptedException {
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
            if (ingredientsRepository.findById(ingredient.getId()).isEmpty()) {
                ingredientsRepository.save(ingredient);
            } else {
                ingredient = ingredientsRepository.findById(ingredient.getId()).orElseThrow();
                ingredientsRepository.saveAndFlush(ingredient);
            }
        }
        return allResults;
    }

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

    @Override
    public void createIngredient(Ingredient ingredient) {
        ingredientsRepository.save(ingredient);
    }

}
