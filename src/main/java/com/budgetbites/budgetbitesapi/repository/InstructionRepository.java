package com.budgetbites.budgetbitesapi.repository;

import com.budgetbites.budgetbitesapi.models.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long> {
}
