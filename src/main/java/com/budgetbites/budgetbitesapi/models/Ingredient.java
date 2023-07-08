package com.budgetbites.budgetbitesapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class Ingredient {

    public Ingredient() {
    }
    @Id
    @Column(name = "ingredient_id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String nameOfBrand;

    @Column(nullable = false)
    private String nameOfRetailer;

    @Column(nullable = false)
    private LocalDateTime validTo;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    @JsonProperty("isValid")
    private boolean isValid = false;

    @Lob
    @Column(nullable = false, length = 512)
    private String description;

    private int count;

    @JsonProperty("brand")
    private void unpackBrandFromNestedObject(Map<String, String> brand) {
        nameOfBrand = brand.get("name");
    }

    @JsonProperty("retailer")
    private void unpackRetailerFromNestedObject(Map<String, String> retailer) {
        nameOfRetailer = retailer.get("name");
    }

    @JsonProperty("product")
    private void unpackProductFromNestedObject(Map<String, String> product) {
        title = product.get("name");
    }
}
