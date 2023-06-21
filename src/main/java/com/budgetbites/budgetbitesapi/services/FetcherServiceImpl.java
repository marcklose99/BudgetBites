package com.budgetbites.budgetbitesapi.services;


import com.budgetbites.budgetbitesapi.models.Ingredient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FetcherServiceImpl implements FetcherService {

    private int postalCode;

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;


    @Override
    public List<Ingredient> getIngredients(int postalCode) {
        this.postalCode = postalCode;
        int fetchResultCount = 0;
        int totalCount = 0;
        JsonNode response = fetch(fetchResultCount);
        List<Ingredient> allResults = new ArrayList<>();

        if (response != null)
            totalCount = response.get("totalResults").intValue();

        while (fetchResultCount < totalCount) {
            response = response.get("results");
            fetchResultCount += response.size();
            try {
                List<Ingredient> mappedIngredients = new ArrayList<>(Arrays.stream(objectMapper.readValue(String.valueOf(response), Ingredient[].class)).toList());
                mappedIngredients = mappedIngredients
                        .stream()
                        .filter(ingredient -> !ingredient.getNameOfRetailer().equals("Globus"))
                        .toList();
                allResults.addAll(mappedIngredients);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            response = fetch(fetchResultCount);
        }
        return allResults;
    }

    /**
     * Retrieves the response from a website.
     *
     * @param offset the number of results already fetched.
     * @return the response as a JsonNode.
     */
    @Override
    public JsonNode fetch(int offset) {
        String url = String.format(String.format("https://api.marktguru.de/api/v1/industries/supermaerkte/offers?as=mobile&limit=512&offset=%d&zipCode=%d", offset, this.postalCode));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(
                        "Content-Type", "application/json",
                        "X-ApiKey", "8Kk+pmbf7TgJ9nVj2cXeA7P5zBGv8iuutVVMRfOfvNE=",
                        "X-ClientKey", "Qs5EM9EteUFUcv6Wx/+omXnHmVMn1PH789u392ewfNE="
                )
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readTree(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
