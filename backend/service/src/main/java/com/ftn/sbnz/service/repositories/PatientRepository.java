package com.ftn.sbnz.service.repositories;

import com.ftn.sbnz.model.domain.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientProfile, Long> {

  Optional<PatientProfile> findByJmbg(String jmbg);

  List<PatientProfile> findByNameContainingIgnoreCaseOrJmbgContaining(String name, String jmbg);
}
