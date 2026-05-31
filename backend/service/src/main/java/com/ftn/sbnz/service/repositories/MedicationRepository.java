package com.ftn.sbnz.service.repositories;

import com.ftn.sbnz.model.domain.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}