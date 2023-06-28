package com.budgetbites.budgetbitesapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Address {


    @Id
    private long id;

    private LocalDate latestFetch;


    public Address(long id) {
        this.id = id;
        this.latestFetch = LocalDate.now();
    }

    public Address() {
    }

    public LocalDate getLatestFetchDate() {
        return this.latestFetch;
    }

    public long getId() {
        return id;
    }

    public void setLatestFetch(LocalDate latestFetch) {
        this.latestFetch = latestFetch;
    }
}
