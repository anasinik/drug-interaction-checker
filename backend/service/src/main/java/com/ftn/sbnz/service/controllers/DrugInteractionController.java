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

        SafetyReport report = drugInteractionService.checkInteractions(dto);
        if (report == null) {
            SafetyReport safe = new SafetyReport();
            safe.setHighestSeverity(SeverityLevel.MILD);
            safe.setSummary("No significant interactions detected.");
            safe.setRecommendation("Medication can be dispensed safely.");
            return ResponseEntity.ok(safe);
        }
        return ResponseEntity.ok(report);
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

    @GetMapping("/contraindication-explanations")
    public ResponseEntity<List<ContraindicationExplanation>> explainContraindication(
            @RequestParam Long patientId,
            @RequestParam String medicationName) {

        return ResponseEntity.ok(
                drugInteractionService.explainContraindication(patientId, medicationName));
    }
}