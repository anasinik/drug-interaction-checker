package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.domain.PatientProfile;
import com.ftn.sbnz.service.repositories.PatientRepository;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class PatientController {

  private final PatientRepository patientRepository;

  @GetMapping
  public List<PatientProfile> getAll() {
    return patientRepository.findAll();
  }

  @GetMapping("/search")
  public List<PatientProfile> search(@RequestParam String q) {
    return patientRepository.findByNameContainingIgnoreCaseOrJmbgContaining(q, q);
  }

  @GetMapping("/jmbg/{jmbg}")
  public ResponseEntity<PatientProfile> getByJmbg(@PathVariable String jmbg) {
    return patientRepository.findByJmbg(jmbg)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<PatientProfile> getById(@PathVariable Long id) {
    return patientRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}