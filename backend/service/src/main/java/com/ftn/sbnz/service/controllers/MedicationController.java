package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.domain.Medication;
import com.ftn.sbnz.service.repositories.MedicationRepository;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class MedicationController {

  private final MedicationRepository medicationRepository;

  @GetMapping
  public List<Medication> getAll() {
    return medicationRepository.findAll();
  }
}