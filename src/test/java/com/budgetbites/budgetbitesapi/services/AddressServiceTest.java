package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Address;
import com.budgetbites.budgetbitesapi.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddressServiceTest {

    private AddressService addressService;
    private AddressRepository mockedAddressRepository;

    @BeforeEach
    void setUp() {
        mockedAddressRepository = mock(AddressRepository.class);
        addressService = new AddressService(mockedAddressRepository);
    }

    @Test
    void isPostalCodeActive_ShouldReturnTrue_WhenFetchDateIsExpired() {
        // Given
        Address address = new Address(12459);
        address.setLatestFetch(LocalDate.of(2000, 1, 1));
        when(mockedAddressRepository.findById(12459L)).thenReturn(Optional.of(address));

        // When
        boolean isDateExpired = addressService.isPostalCodeActive(12459);

        // Then
        assertTrue(isDateExpired);
    }

    @Test
    void isPostalCodeActive_ShouldReturnFalse_WhenFetchDateIsNotExpired() {
        // Given
        Address address = new Address(12459);
        when(mockedAddressRepository.findById(12459L)).thenReturn(Optional.of(address));

        // When
        boolean isDateExpired = addressService.isPostalCodeActive(12459);

        // Then
        assertFalse(isDateExpired);
    }
}