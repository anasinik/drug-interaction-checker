package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.domain.*;
import com.ftn.sbnz.service.components.CausalFactsInitializer;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DrugInteractionService {

        @Autowired
        private KieContainer kieContainer;

        @Autowired
        private CausalFactsInitializer causalFactsInitializer;

        public SafetyReport checkInteractions(MedicationRequest request) {
                KieSession session = kieContainer.newKieSession();
                try {
                        session.insert(request.getPatient());
                        session.insert(request.getNewMedication());
                        session.insert(request);

                        session.fireAllRules();

                        Collection<?> reports = session.getObjects(obj -> obj instanceof SafetyReport);
                        return reports.stream()
                                        .map(r -> (SafetyReport) r)
                                        .findFirst()
                                        .orElse(null);
                } finally {
                        session.dispose();
                }
        }

        public List<DetectedInteraction> getDetectedInteractions(MedicationRequest request) {
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
                        PatientProfile patient, String medicationName) {

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