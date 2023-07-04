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
    public boolean isPostalCodeActive(long postalCode) {
        Optional<Address> address = addressRepository.findById(postalCode);
        if(address.isEmpty()) {
            addressRepository.save(new Address(postalCode));
            return false;
        }
        return isFetchDateExpired(address.get());
    }

    private boolean isFetchDateExpired(Address address) {
        LocalDate now = LocalDate.now();
        LocalDate oneDayAgo = now.minus(1, ChronoUnit.DAYS);
        if(address.getLatestFetchDate().isBefore(oneDayAgo)) {
            address.setLatestFetch(now);
            addressRepository.save(address);
            return true;
        }
        return false;
    }

}
