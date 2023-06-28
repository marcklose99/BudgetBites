package com.budgetbites.budgetbitesapi.repository;

import com.budgetbites.budgetbitesapi.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
