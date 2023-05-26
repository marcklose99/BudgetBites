package com.budgetbites.budgetbitesapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Entity
@Table
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {

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

    @Column(name = "valid_to", nullable = false)
    private Date validTo;

    @Column(name = "valid_from", nullable = false)
    private Date validFrom;

    @Column(name = "is_valid", nullable = false)
    private boolean isValid = false;

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
