package com.budgetbites.budgetbitesapi.services;

import com.budgetbites.budgetbitesapi.models.Address;
import com.budgetbites.budgetbitesapi.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    public boolean isRecentlyFetched(long postalCode) {
        Optional<Address> address = addressRepository.findById(postalCode);
        return address.isPresent() && !addressExpired(address.get());
    }

    private boolean addressExpired(Address address) {
        LocalDate fiveDaysAgo = LocalDate.now().minus(5, ChronoUnit.DAYS);
        return address.getLatestFetchDate().isAfter(fiveDaysAgo);
    }

}
