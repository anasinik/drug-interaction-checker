package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.domain.*;
import com.ftn.sbnz.service.dtos.InteractionRequestDto;
import com.ftn.sbnz.service.services.DrugInteractionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interactions")
@CrossOrigin(origins = "*")
public class DrugInteractionController {

    @Autowired
    private DrugInteractionService drugInteractionService;

    @GetMapping("/safety-report")
    public ResponseEntity<SafetyReport> checkInteractions(
            @RequestParam Long patientId,
            @RequestParam Long medicationId) {

        InteractionRequestDto dto = new InteractionRequestDto();
        dto.setPatientId(patientId);
        dto.setMedicationId(medicationId);

        return ResponseEntity.ok(drugInteractionService.checkInteractions(dto));
    }

    @GetMapping
    public ResponseEntity<List<DetectedInteraction>> getDetectedInteractions(
            @RequestParam Long patientId,
            @RequestParam Long medicationId) {

        InteractionRequestDto dto = new InteractionRequestDto();
        dto.setPatientId(patientId);
        dto.setMedicationId(medicationId);

        return ResponseEntity.ok(drugInteractionService.getDetectedInteractions(dto));
    }

    @GetMapping("/severity-explanation")
    public ResponseEntity<List<InteractionSeverityExplanation>> explainSeverity(
            @RequestParam String factor,
            @RequestParam String medicationName) {

        return ResponseEntity.ok(drugInteractionService.explainSeverity(factor, medicationName));
    }
}