package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.domain.*;
import com.ftn.sbnz.service.components.CausalFactsInitializer;
import com.ftn.sbnz.service.dtos.InteractionRequestDto;
import com.ftn.sbnz.service.repositories.MedicationRepository;
import com.ftn.sbnz.service.repositories.PatientRepository;

import lombok.AllArgsConstructor;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DrugInteractionService {

        private final KieContainer kieContainer;
        private final CausalFactsInitializer causalFactsInitializer;
        private final PatientRepository patientRepository;
        private final MedicationRepository medicationRepository;

        private MedicationRequest resolve(InteractionRequestDto dto) {
                PatientProfile patient = patientRepository.findById(dto.getPatientId())
                                .orElseThrow(() -> new RuntimeException("Patient not found: " + dto.getPatientId()));
                Medication medication = medicationRepository.findById(dto.getMedicationId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Medication not found: " + dto.getMedicationId()));
                return new MedicationRequest(patient, medication);
        }

        public SafetyReport checkInteractions(InteractionRequestDto dto) {
                MedicationRequest request = resolve(dto);
                KieSession session = kieContainer.newKieSession();
                try {
                        session.insert(request.getPatient());
                        session.insert(request.getNewMedication());
                        session.insert(request);
                        session.fireAllRules();

                        List<SafetyReport> reports = session.getObjects(obj -> obj instanceof SafetyReport)
                                        .stream()
                                        .map(r -> (SafetyReport) r)
                                        .collect(Collectors.toList());

                        return reports.stream()
                                        .max(Comparator.comparingInt(r -> r.getHighestSeverity().getScore()))
                                        .orElse(null);
                } finally {
                        session.dispose();
                }
        }

        public List<DetectedInteraction> getDetectedInteractions(InteractionRequestDto dto) {
                MedicationRequest request = resolve(dto);
                KieSession session = kieContainer.newKieSession();
                try {
                        session.insert(request.getPatient());
                        session.insert(request.getNewMedication());
                        session.insert(request);
                        session.fireAllRules();

                        return session.getObjects(obj -> obj instanceof DetectedInteraction)
                                        .stream()
                                        .map(obj -> (DetectedInteraction) obj)
                                        .collect(Collectors.toList());
                } finally {
                        session.dispose();
                }
        }

        public List<ContraindicationExplanation> explainContraindication(
                        Long patientId, String medicationName) {

                PatientProfile patient = patientRepository.findById(patientId)
                                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));

                KieSession session = kieContainer.newKieSession();
                try {
                        causalFactsInitializer.insertFacts(session);
                        session.insert(new ExplanationRequest(patient, medicationName));
                        session.fireAllRules();

                        return session.getObjects(obj -> obj instanceof ContraindicationExplanation)
                                        .stream()
                                        .map(obj -> (ContraindicationExplanation) obj)
                                        .collect(Collectors.toList());
                } finally {
                        session.dispose();
                }
        }
}