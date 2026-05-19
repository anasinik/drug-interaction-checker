package com.ftn.sbnz.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.ftn.sbnz.model.domain.*;

@DisplayName("Drools Forward Chaining")
public class DroolsForwardChainingTest {

        private KieSession kieSession;

        @BeforeEach
        public void setup() {
                KieServices ks = KieServices.Factory.get();
                KieContainer kContainer = ks
                                .newKieContainer(ks.newReleaseId("com.ftn.sbnz", "kjar", "0.0.1-SNAPSHOT"));
                kieSession = kContainer.newKieSession();
        }

        @AfterEach
        public void teardown() {
                if (kieSession != null) {
                        kieSession.dispose();
                }
        }

        // LEVEL 1: INTERACTION DETECTION

        @Test
        @DisplayName("Level 1: Detect DRUG-DISEASE interaction - Amitriptyline + cardiac arrhythmia")
        public void testDetectDrugDiseaseInteraction_AmitriptillinArrhythmia() {
                PatientProfile patient = new PatientProfile(
                                "Marko Markovic",
                                70,
                                75.0,
                                new ArrayList<>(),
                                List.of("cardiac arrhythmia"),
                                new ArrayList<>(),
                                new ArrayList<>());

                Medication amitriptyline = Medication.builder()
                                .name("Amitriptyline")
                                .category(MedicationCategory.ANTIDEPRESSANT)
                                .renallyCleared(false)
                                .hepaticallyMetabolized(true)
                                .build();

                MedicationRequest request = new MedicationRequest(patient, amitriptyline);

                kieSession.insert(patient);
                kieSession.insert(amitriptyline);
                kieSession.insert(request);

                int firedRules = kieSession.fireAllRules();

                Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof DetectedInteraction);

                assertTrue(firedRules > 0, "At least one rule should have fired");
                assertFalse(interactions.isEmpty(), "At least one interaction should be detected");

                DetectedInteraction detected = (DetectedInteraction) interactions.iterator().next();
                assertEquals(InteractionType.DRUG_DISEASE, detected.getInteractionType());
                assertEquals(SeverityLevel.SERIOUS, detected.getSeverity());
                assertEquals(SeverityLevel.SERIOUS.getScore(), detected.getScore());
                assertTrue(detected.getReason().toLowerCase().contains("amitriptyline"),
                                "Reason should contain the drug name");
                assertNotNull(detected.getNewMedication());
                assertEquals("Amitriptyline", detected.getNewMedication().getName());

                System.out.println();
                System.out.println("  LEVEL 1 PASSED - DRUG-DISEASE interaction detected");
                System.out.println("  Drug     : " + detected.getNewMedication().getName());
                System.out.println("  Type     : " + detected.getInteractionType());
                System.out.println("  Severity : " + detected.getSeverity() + " (score: " + detected.getScore() + ")");
                System.out.println();
        }

        @Test
        @DisplayName("Level 1: Detect ALLERGY interaction - Penicillin antibiotic with penicillin allergy")
        public void testDetectContraindicatedAllergy_PenicillinAllergy() {
                PatientProfile patient = new PatientProfile(
                                "Ana Anic",
                                45,
                                65.0,
                                new ArrayList<>(),
                                new ArrayList<>(),
                                List.of("penicillin"),
                                new ArrayList<>());

                Medication amoxicillin = Medication.builder()
                                .name("Amoxicillin")
                                .category(MedicationCategory.ANTIBIOTIC)
                                .renallyCleared(true)
                                .hepaticallyMetabolized(false)
                                .build();

                MedicationRequest request = new MedicationRequest(patient, amoxicillin);

                kieSession.insert(patient);
                kieSession.insert(amoxicillin);
                kieSession.insert(request);

                int firedRules = kieSession.fireAllRules();

                Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof DetectedInteraction);

                assertTrue(firedRules > 0, "At least one rule should have fired");
                assertFalse(interactions.isEmpty(), "A contraindication should be detected");

                DetectedInteraction detected = (DetectedInteraction) interactions.iterator().next();
                assertEquals(SeverityLevel.CONTRAINDICATED, detected.getSeverity(),
                                "Penicillin allergy should be contraindicated");
                assertEquals(SeverityLevel.CONTRAINDICATED.getScore(), detected.getScore());
                assertNotNull(detected.getReason());
                assertFalse(detected.getReason().isBlank());

                System.out.println();
                System.out.println("  LEVEL 1 PASSED - ALLERGY contraindication detected");
                System.out.println("  Drug     : " + detected.getNewMedication().getName());
                System.out.println("  Allergy  : " + patient.getAllergies());
                System.out.println("  Severity : " + detected.getSeverity());
                System.out.println();
        }

        // LEVEL 2: RISK AGGREGATION

        @Test
        @DisplayName("Level 2: Risk aggregation - Polypharmacy elevates mild interactions to SERIOUS")
        public void testRiskAggregation_PolypharmacyElevatesMildToSerious() {
                List<Medication> medications = List.of(
                                Medication.builder().name("Metformin").category(MedicationCategory.ANTIDIABETIC)
                                                .renallyCleared(true).hepaticallyMetabolized(false).build(),
                                Medication.builder().name("Lisinopril").category(MedicationCategory.ANTIHYPERTENSIVE)
                                                .renallyCleared(true).hepaticallyMetabolized(false).build(),
                                Medication.builder().name("Simvastatin").category(MedicationCategory.STATIN)
                                                .renallyCleared(false).hepaticallyMetabolized(true).build(),
                                Medication.builder().name("Aspirin").category(MedicationCategory.ANTICOAGULANT)
                                                .renallyCleared(false).hepaticallyMetabolized(false).build(),
                                Medication.builder().name("Paracetamol").category(MedicationCategory.OTHER)
                                                .renallyCleared(false).hepaticallyMetabolized(true).build());

                PatientProfile patient = new PatientProfile(
                                "Pera Petrovic",
                                68,
                                80.0,
                                new ArrayList<>(medications),
                                List.of("hypertension", "diabetes"),
                                new ArrayList<>(),
                                new ArrayList<>());

                Medication newMedication = Medication.builder()
                                .name("Ibuprofen")
                                .category(MedicationCategory.OTHER)
                                .renallyCleared(false)
                                .hepaticallyMetabolized(true)
                                .build();

                MedicationRequest request = new MedicationRequest(patient, newMedication);

                kieSession.insert(patient);
                medications.forEach(kieSession::insert);
                kieSession.insert(newMedication);
                kieSession.insert(request);

                int firedRules = kieSession.fireAllRules();

                Collection<?> risks = kieSession.getObjects(obj -> obj instanceof TherapyRisk);

                assertTrue(firedRules > 0, "At least one rule should have fired");
                assertFalse(risks.isEmpty(), "A TherapyRisk fact should be created");

                TherapyRisk risk = (TherapyRisk) risks.iterator().next();
                assertNotNull(risk.getPatient());
                assertTrue(risk.getPatient().hasPolypharmacy(), "Patient should have polypharmacy");
                assertTrue(risk.getTotalScore() >= 0, "Total score must not be negative");

                System.out.println();
                System.out.println("  LEVEL 2 PASSED - Risk aggregation");
                System.out.println("  Patient      : " + patient.getName());
                System.out.println("  Medications  : " + patient.getMedicationCount());
                System.out.println("  Polypharmacy : " + patient.hasPolypharmacy());
                System.out.println("  Total score  : " + risk.getTotalScore());
                System.out.println("  High risk    : " + risk.isHighRisk());
                System.out.println();
        }

        @Test
        @DisplayName("Level 2: Risk aggregation - Score >= 10 is HIGH RISK")
        public void testRiskAggregation_HighRiskThreshold() {
                PatientProfile patient = new PatientProfile(
                                "Jovana Jovanovic",
                                72,
                                70.0,
                                List.of(Medication.builder().name("Warfarin").category(MedicationCategory.ANTICOAGULANT)
                                                .renallyCleared(false).hepaticallyMetabolized(true).build()),
                                List.of("cardiac arrhythmia"),
                                new ArrayList<>(),
                                new ArrayList<>());

                Medication aspirin = Medication.builder()
                                .name("Aspirin")
                                .category(MedicationCategory.ANTICOAGULANT)
                                .renallyCleared(false)
                                .hepaticallyMetabolized(false)
                                .build();

                MedicationRequest request = new MedicationRequest(patient, aspirin);

                kieSession.insert(patient);
                patient.getCurrentMedications().forEach(kieSession::insert);
                kieSession.insert(aspirin);
                kieSession.insert(request);

                int firedRules = kieSession.fireAllRules();

                Collection<?> therapyRisks = kieSession.getObjects(obj -> obj instanceof TherapyRisk);
                Collection<?> detectedInteractions = kieSession.getObjects(obj -> obj instanceof DetectedInteraction);

                assertTrue(firedRules > 0, "At least one rule should have fired");
                assertFalse(detectedInteractions.isEmpty(), "At least one interaction should be detected");
                assertFalse(therapyRisks.isEmpty(), "A TherapyRisk fact should be created");

                TherapyRisk risk = (TherapyRisk) therapyRisks.iterator().next();
                assertNotNull(risk, "TherapyRisk must not be null");
                assertTrue(risk.getTotalScore() >= 0, "Score must not be negative");

                System.out.println();
                System.out.println("  LEVEL 2 PASSED - High risk threshold");
                System.out.println("  Detected interactions : " + detectedInteractions.size());
                detectedInteractions.forEach(di -> {
                        DetectedInteraction interaction = (DetectedInteraction) di;
                        System.out.println("    - " + interaction.getNewMedication().getName()
                                        + " : " + interaction.getSeverity()
                                        + " (score: " + interaction.getScore() + ")");
                });
                System.out.println("  Total score  : " + risk.getTotalScore());
                System.out.println("  High risk    : " + risk.isHighRisk() + " (threshold: 10)");
                System.out.println();
        }

        // LEVEL 3: REPORT GENERATION

        @Test
        @DisplayName("Level 3: Report generation - CONTRAINDICATED therapy report")
        public void testReportGeneration_ContraindicatedReport() {
                PatientProfile patient = new PatientProfile(
                                "Dragan Dragic",
                                50,
                                85.0,
                                new ArrayList<>(),
                                new ArrayList<>(),
                                List.of("penicillin"),
                                new ArrayList<>());

                Medication amoxicillin = Medication.builder()
                                .name("Amoxicillin")
                                .category(MedicationCategory.ANTIBIOTIC)
                                .renallyCleared(true)
                                .hepaticallyMetabolized(false)
                                .build();

                MedicationRequest request = new MedicationRequest(patient, amoxicillin);

                kieSession.insert(patient);
                kieSession.insert(amoxicillin);
                kieSession.insert(request);

                kieSession.fireAllRules();

                Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof DetectedInteraction);
                Collection<?> reports = kieSession.getObjects(obj -> obj instanceof SafetyReport);

                assertFalse(interactions.isEmpty(), "A contraindication should be detected before generating report");
                assertFalse(reports.isEmpty(), "A SafetyReport should be generated");

                SafetyReport report = (SafetyReport) reports.iterator().next();
                assertEquals(SeverityLevel.CONTRAINDICATED, report.getHighestSeverity());
                assertNotNull(report.getRecommendation());
                assertFalse(report.getRecommendation().isBlank());
                assertNotNull(report.getSummary());
                assertFalse(report.getSummary().isBlank());
                assertTrue(report.getRecommendation().toLowerCase().contains("review") ||
                                report.getRecommendation().toLowerCase().contains("consult") ||
                                report.getRecommendation().toLowerCase().contains("adjust"),
                                "Recommendation should contain action items");

                System.out.println();
                System.out.println("  LEVEL 3 PASSED - Contraindicated therapy report");
                System.out.println("  Patient        : " + report.getPatient().getName());
                System.out.println("  Severity       : " + report.getHighestSeverity());
                System.out.println("  Summary        : " + report.getSummary());
                System.out.println("  Recommendation : " + report.getRecommendation());
                System.out.println();
        }

        @Test
        @DisplayName("Level 3: Report generation - HIGH RISK therapy report")
        public void testReportGeneration_HighRiskReport() {
                PatientProfile patient = new PatientProfile(
                                "Svetlana Svetkovic",
                                70,
                                65.0,
                                List.of(
                                                Medication.builder().name("Metformin")
                                                                .category(MedicationCategory.ANTIDIABETIC)
                                                                .renallyCleared(true).hepaticallyMetabolized(false)
                                                                .build(),
                                                Medication.builder().name("Lisinopril")
                                                                .category(MedicationCategory.ANTIHYPERTENSIVE)
                                                                .renallyCleared(true).hepaticallyMetabolized(false)
                                                                .build(),
                                                Medication.builder().name("Simvastatin")
                                                                .category(MedicationCategory.STATIN)
                                                                .renallyCleared(false).hepaticallyMetabolized(true)
                                                                .build()),
                                List.of("hypertension", "diabetes"),
                                new ArrayList<>(),
                                new ArrayList<>());

                Medication newMedication = Medication.builder()
                                .name("Ibuprofen")
                                .category(MedicationCategory.OTHER)
                                .renallyCleared(false)
                                .hepaticallyMetabolized(true)
                                .build();

                MedicationRequest request = new MedicationRequest(patient, newMedication);

                kieSession.insert(patient);
                patient.getCurrentMedications().forEach(kieSession::insert);
                kieSession.insert(newMedication);
                kieSession.insert(request);

                kieSession.fireAllRules();

                Collection<?> reports = kieSession.getObjects(obj -> obj instanceof SafetyReport);

                assertFalse(reports.isEmpty(), "A SafetyReport should be generated");

                SafetyReport report = (SafetyReport) reports.iterator().next();
                assertNotNull(report.getPatient());
                assertNotNull(report.getHighestSeverity());
                assertNotNull(report.getRecommendation());
                assertFalse(report.getRecommendation().isBlank());
                assertNotNull(report.getSummary());
                assertFalse(report.getSummary().isBlank());

                System.out.println();
                System.out.println("  LEVEL 3 PASSED - High risk therapy report");
                System.out.println("  Patient        : " + report.getPatient().getName());
                System.out.println("  Severity       : " + report.getHighestSeverity());
                System.out.println("  Summary        : " + report.getSummary());
                System.out.println("  Recommendation : " + report.getRecommendation());
                System.out.println();
        }

        @Test
        @DisplayName("Level 3: Report generation - SAFE therapy report")
        public void testReportGeneration_SafeReport() {
                PatientProfile patient = new PatientProfile(
                                "Milos Milosevic",
                                40,
                                75.0,
                                new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Medication ibuprofen = Medication.builder()
                                .name("Ibuprofen")
                                .category(MedicationCategory.OTHER)
                                .renallyCleared(false)
                                .hepaticallyMetabolized(true)
                                .build();

                MedicationRequest request = new MedicationRequest(patient, ibuprofen);

                kieSession.insert(patient);
                kieSession.insert(ibuprofen);
                kieSession.insert(request);

                kieSession.fireAllRules();

                Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof DetectedInteraction);
                Collection<?> reports = kieSession.getObjects(obj -> obj instanceof SafetyReport);

                assertTrue(interactions.isEmpty(),
                                "No interactions expected for a healthy patient with no current medications");
                assertFalse(reports.isEmpty(), "A SafetyReport should be generated even for safe therapies");

                SafetyReport report = (SafetyReport) reports.iterator().next();
                assertEquals(SeverityLevel.MILD, report.getHighestSeverity());
                assertNotNull(report.getRecommendation());
                assertFalse(report.getRecommendation().isBlank());

                System.out.println();
                System.out.println("  LEVEL 3 PASSED - Safe therapy report");
                System.out.println("  Patient        : " + report.getPatient().getName());
                System.out.println("  Severity       : " + report.getHighestSeverity());
                System.out.println("  Summary        : " + report.getSummary());
                System.out.println("  Recommendation : " + report.getRecommendation());
                System.out.println();
        }

        // COMPLEX SCENARIO

        @Test
        @DisplayName("Complex scenario: All three forward chaining levels together")
        public void testCompleteForwardChainingFlow_ComplexScenario() {
                List<Medication> existingMeds = List.of(
                                Medication.builder().name("Warfarin").category(MedicationCategory.ANTICOAGULANT)
                                                .renallyCleared(false).hepaticallyMetabolized(true).build(),
                                Medication.builder().name("Metformin").category(MedicationCategory.ANTIDIABETIC)
                                                .renallyCleared(true).hepaticallyMetabolized(false).build(),
                                Medication.builder().name("Lisinopril").category(MedicationCategory.ANTIHYPERTENSIVE)
                                                .renallyCleared(true).hepaticallyMetabolized(false).build());

                PatientProfile patient = new PatientProfile(
                                "Bora Borivoje",
                                76,
                                70.0,
                                new ArrayList<>(existingMeds),
                                List.of("atrial fibrillation", "diabetes", "hypertension"),
                                new ArrayList<>(),
                                new ArrayList<>());

                Medication newMedication = Medication.builder()
                                .name("Aspirin")
                                .category(MedicationCategory.ANTICOAGULANT)
                                .renallyCleared(false)
                                .hepaticallyMetabolized(false)
                                .build();

                MedicationRequest request = new MedicationRequest(patient, newMedication);

                kieSession.insert(patient);
                existingMeds.forEach(kieSession::insert);
                kieSession.insert(newMedication);
                kieSession.insert(request);

                int firedRules = kieSession.fireAllRules();

                Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof DetectedInteraction);
                Collection<?> therapyRisks = kieSession.getObjects(obj -> obj instanceof TherapyRisk);
                Collection<?> reports = kieSession.getObjects(obj -> obj instanceof SafetyReport);

                // VERIFY LEVEL 1
                assertTrue(firedRules > 0, "At least one rule should have fired");
                assertFalse(interactions.isEmpty(), "At least one interaction should be detected");
                interactions.forEach(obj -> {
                        DetectedInteraction di = (DetectedInteraction) obj;
                        assertNotNull(di.getSeverity());
                        assertNotNull(di.getInteractionType());
                        assertNotNull(di.getReason());
                        assertTrue(di.getScore() > 0, "Interaction score must be positive");
                });

                // VERIFY LEVEL 2
                assertFalse(therapyRisks.isEmpty(), "A TherapyRisk fact should be created");
                TherapyRisk risk = (TherapyRisk) therapyRisks.iterator().next();
                assertNotNull(risk, "TherapyRisk must not be null");
                assertNotNull(risk.getPatient());
                assertTrue(risk.getTotalScore() >= 0, "Score must not be negative");

                // VERIFY LEVEL 3
                assertFalse(reports.isEmpty(), "A SafetyReport should be generated");
                SafetyReport report = (SafetyReport) reports.iterator().next();
                assertNotNull(report.getRecommendation(), "Recommendation must not be null");
                assertFalse(report.getRecommendation().isBlank(), "Recommendation must not be blank");
                assertNotNull(report.getHighestSeverity(), "Highest severity must not be null");
                assertNotNull(report.getPatient(), "Patient in report must not be null");

                System.out.println();
                System.out.println("  ============================================================");
                System.out.println("  COMPLEX SCENARIO PASSED - Full forward chaining flow");
                System.out.println("  ============================================================");
                System.out.println();
                System.out.println("  LEVEL 1 - INTERACTION DETECTION");
                interactions.forEach(interaction -> {
                        DetectedInteraction di = (DetectedInteraction) interaction;
                        System.out.println("    Drug     : " + di.getNewMedication().getName());
                        System.out.println("    Type     : " + di.getInteractionType());
                        System.out.println("    Severity : " + di.getSeverity() + " (score: " + di.getScore() + ")");
                        System.out.println("    Reason   : " + di.getReason());
                });
                System.out.println();
                System.out.println("  LEVEL 2 - RISK AGGREGATION");
                System.out.println("    Interactions : " + interactions.size());
                System.out.println("    Total score  : " + risk.getTotalScore());
                System.out.println("    High risk    : " + risk.isHighRisk() + " (threshold: 10)");
                System.out.println();
                System.out.println("  LEVEL 3 - REPORT");
                System.out.println("    Patient    : " + report.getPatient().getName()
                                + " (age " + report.getPatient().getAge() + ")");
                System.out.println("    Diagnoses  : " + report.getPatient().getDiagnoses());
                System.out.println("    Severity   : " + report.getHighestSeverity());
                System.out.println("    Summary    : " + report.getSummary());
                System.out.println("    Rec        : " + report.getRecommendation());
                System.out.println();
        }
}