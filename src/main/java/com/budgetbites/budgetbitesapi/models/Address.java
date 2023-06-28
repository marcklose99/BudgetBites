package com.budgetbites.budgetbitesapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Address {


    @Id
    private long id;

    private LocalDate latestFetch;

    public LocalDate getLatestFetchDate() {
        return this.latestFetch;
    }

}
