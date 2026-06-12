// package com.ftn.sbnz.service;

// import static org.junit.jupiter.api.Assertions.*;

// import java.util.ArrayList;
// import java.util.Collection;
// import java.util.List;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.kie.api.KieServices;
// import org.kie.api.runtime.KieContainer;
// import org.kie.api.runtime.KieSession;

// import com.ftn.sbnz.model.domain.*;

// @DisplayName("Drools Forward Chaining")
// public class DroolsForwardChainingTest {

// private KieSession kieSession;

// @BeforeEach
// public void setup() {
// KieServices ks = KieServices.Factory.get();
// KieContainer kContainer = ks.newKieContainer(ks.newReleaseId("com.ftn.sbnz",
// "kjar", "0.0.1-SNAPSHOT"));
// kieSession = kContainer.newKieSession();
// }

// @AfterEach
// public void teardown() {
// if (kieSession != null) {
// kieSession.dispose();
// }
// }

// // LEVEL 1: INTERACTION DETECTION

// @Test
// @DisplayName("Level 1: Detect DRUG-DISEASE interaction - Amitriptyline +
// cardiac arrhythmia")
// public void testDetectDrugDiseaseInteraction_AmitriptillinArrhythmia() {
// PatientProfile patient = new PatientProfile(
// "Marko Markovic", 70, 75.0,
// new ArrayList<>(),
// List.of("cardiac arrhythmia"),
// new ArrayList<>(),
// new ArrayList<>());

// Medication amitriptyline = Medication.builder()
// .name("Amitriptyline")
// .category(MedicationCategory.ANTIDEPRESSANT)
// .renallyCleared(false)
// .hepaticallyMetabolized(true)
// .build();

// MedicationRequest request = new MedicationRequest(patient, amitriptyline);

// kieSession.insert(patient);
// kieSession.insert(amitriptyline);
// kieSession.insert(request);

// int firedRules = kieSession.fireAllRules();

// Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof
// DetectedInteraction);

// assertTrue(firedRules > 0, "At least one rule should have fired");
// assertFalse(interactions.isEmpty(), "At least one interaction should be
// detected");

// DetectedInteraction detected = (DetectedInteraction)
// interactions.iterator().next();
// assertEquals(InteractionType.DRUG_DISEASE, detected.getInteractionType());
// assertEquals(SeverityLevel.SERIOUS, detected.getSeverity());
// assertEquals(SeverityLevel.SERIOUS.getScore(), detected.getScore());
// assertNotNull(detected.getNewMedication());
// assertEquals("Amitriptyline", detected.getNewMedication().getName());

// System.out.println("LEVEL 1 PASSED - drug-disease interaction detected");
// System.out.println("drug: " + detected.getNewMedication().getName());
// System.out.println("type: " + detected.getInteractionType());
// System.out.println("severity: " + detected.getSeverity() + " (score: " +
// detected.getScore() + ")");
// }

// @Test
// @DisplayName("Level 1: Detect ALLERGY interaction - Penicillin antibiotic
// with penicillin allergy")
// public void testDetectContraindicatedAllergy_PenicillinAllergy() {
// PatientProfile patient = new PatientProfile(
// "Ana Anic", 45, 65.0,
// new ArrayList<>(),
// new ArrayList<>(),
// List.of("penicillin"),
// new ArrayList<>());

// Medication amoxicillin = Medication.builder()
// .name("Amoxicillin")
// .category(MedicationCategory.ANTIBIOTIC)
// .renallyCleared(true)
// .hepaticallyMetabolized(false)
// .build();

// MedicationRequest request = new MedicationRequest(patient, amoxicillin);

// kieSession.insert(patient);
// kieSession.insert(amoxicillin);
// kieSession.insert(request);

// int firedRules = kieSession.fireAllRules();

// Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof
// DetectedInteraction);

// assertTrue(firedRules > 0, "At least one rule should have fired");
// assertFalse(interactions.isEmpty(), "A contraindication should be detected");

// DetectedInteraction detected = (DetectedInteraction)
// interactions.iterator().next();
// assertEquals(SeverityLevel.CONTRAINDICATED, detected.getSeverity(),
// "Penicillin allergy should be contraindicated");
// assertEquals(SeverityLevel.CONTRAINDICATED.getScore(), detected.getScore());
// assertNotNull(detected.getReason());
// assertFalse(detected.getReason().isBlank());

// System.out.println("LEVEL 1 PASSED - allergy contraindication detected");
// System.out.println("drug: " + detected.getNewMedication().getName());
// System.out.println("allergy: " + patient.getAllergies());
// System.out.println("severity: " + detected.getSeverity());
// }

// // LEVEL 2: RISK AGGREGATION

// @Test
// @DisplayName("Level 2: Risk aggregation - Polypharmacy elevates mild
// interactions to SERIOUS")
// public void testRiskAggregation_PolypharmacyElevatesMildToSerious() {
// List<Medication> medications = List.of(
// Medication.builder().name("Metformin").category(MedicationCategory.ANTIDIABETIC)
// .renallyCleared(true).hepaticallyMetabolized(false).build(),
// Medication.builder().name("Lisinopril").category(MedicationCategory.ANTIHYPERTENSIVE)
// .renallyCleared(true).hepaticallyMetabolized(false).build(),
// Medication.builder().name("Simvastatin").category(MedicationCategory.STATIN)
// .renallyCleared(false).hepaticallyMetabolized(true).build(),
// Medication.builder().name("Aspirin").category(MedicationCategory.ANTICOAGULANT)
// .renallyCleared(false).hepaticallyMetabolized(false).build(),
// Medication.builder().name("Paracetamol").category(MedicationCategory.OTHER)
// .renallyCleared(false).hepaticallyMetabolized(true).build());

// PatientProfile patient = new PatientProfile(
// "Pera Petrovic", 68, 80.0,
// new ArrayList<>(medications),
// List.of("hypertension", "diabetes"),
// new ArrayList<>(),
// new ArrayList<>());

// Medication newMedication = Medication.builder()
// .name("Ibuprofen")
// .category(MedicationCategory.OTHER)
// .renallyCleared(false)
// .hepaticallyMetabolized(true)
// .build();

// MedicationRequest request = new MedicationRequest(patient, newMedication);

// kieSession.insert(patient);
// medications.forEach(kieSession::insert);
// kieSession.insert(newMedication);
// kieSession.insert(request);

// int firedRules = kieSession.fireAllRules();

// Collection<?> risks = kieSession.getObjects(obj -> obj instanceof
// TherapyRisk);

// assertTrue(firedRules > 0, "At least one rule should have fired");
// assertFalse(risks.isEmpty(), "A TherapyRisk fact should be created");

// TherapyRisk risk = (TherapyRisk) risks.iterator().next();
// assertNotNull(risk.getPatient());
// assertTrue(risk.getPatient().hasPolypharmacy(), "Patient should have
// polypharmacy");
// assertTrue(risk.getTotalScore() >= 0, "Total score must not be negative");

// System.out.println("LEVEL 2 PASSED - risk aggregation");
// System.out.println("patient: " + patient.getName());
// System.out.println("medications: " + patient.getMedicationCount() + ",
// polypharmacy: "
// + patient.hasPolypharmacy());
// System.out.println("total score: " + risk.getTotalScore() + ", high risk: " +
// risk.isHighRisk());
// }

// @Test
// @DisplayName("Level 2: Risk aggregation - Score >= 10 is HIGH RISK")
// public void testRiskAggregation_HighRiskThreshold() {
// PatientProfile patient = new PatientProfile(
// "Jovana Jovanovic", 72, 70.0,
// List.of(Medication.builder().name("Warfarin").category(MedicationCategory.ANTICOAGULANT)
// .renallyCleared(false).hepaticallyMetabolized(true).build()),
// List.of("cardiac arrhythmia"),
// new ArrayList<>(),
// new ArrayList<>());

// Medication aspirin = Medication.builder()
// .name("Aspirin")
// .category(MedicationCategory.ANTICOAGULANT)
// .renallyCleared(false)
// .hepaticallyMetabolized(false)
// .build();

// MedicationRequest request = new MedicationRequest(patient, aspirin);

// kieSession.insert(patient);
// patient.getCurrentMedications().forEach(kieSession::insert);
// kieSession.insert(aspirin);
// kieSession.insert(request);

// int firedRules = kieSession.fireAllRules();

// Collection<?> therapyRisks = kieSession.getObjects(obj -> obj instanceof
// TherapyRisk);
// Collection<?> detectedInteractions = kieSession.getObjects(obj -> obj
// instanceof DetectedInteraction);

// assertTrue(firedRules > 0, "At least one rule should have fired");
// assertFalse(detectedInteractions.isEmpty(), "At least one interaction should
// be detected");
// assertFalse(therapyRisks.isEmpty(), "A TherapyRisk fact should be created");

// TherapyRisk risk = (TherapyRisk) therapyRisks.iterator().next();
// assertNotNull(risk, "TherapyRisk must not be null");
// assertTrue(risk.getTotalScore() >= 0, "Score must not be negative");

// System.out.println("LEVEL 2 PASSED - high risk threshold");
// System.out.println("detected interactions: " + detectedInteractions.size());
// detectedInteractions.forEach(di -> {
// DetectedInteraction interaction = (DetectedInteraction) di;
// System.out.println(" " + interaction.getNewMedication().getName() + " - "
// + interaction.getSeverity() + " (score: " + interaction.getScore() + ")");
// });
// System.out.println("total score: " + risk.getTotalScore() + ", high risk: " +
// risk.isHighRisk());
// }

// // LEVEL 3: REPORT GENERATION

// @Test
// @DisplayName("Level 3: Report generation - CONTRAINDICATED therapy report")
// public void testReportGeneration_ContraindicatedReport() {
// PatientProfile patient = new PatientProfile(
// "Dragan Dragic", 50, 85.0,
// new ArrayList<>(),
// new ArrayList<>(),
// List.of("penicillin"),
// new ArrayList<>());

// Medication amoxicillin = Medication.builder()
// .name("Amoxicillin")
// .category(MedicationCategory.ANTIBIOTIC)
// .renallyCleared(true)
// .hepaticallyMetabolized(false)
// .build();

// MedicationRequest request = new MedicationRequest(patient, amoxicillin);

// kieSession.insert(patient);
// kieSession.insert(amoxicillin);
// kieSession.insert(request);

// kieSession.fireAllRules();

// Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof
// DetectedInteraction);
// Collection<?> reports = kieSession.getObjects(obj -> obj instanceof
// SafetyReport);

// assertFalse(interactions.isEmpty(), "A contraindication should be detected
// before generating report");
// assertFalse(reports.isEmpty(), "A SafetyReport should be generated");

// SafetyReport report = (SafetyReport) reports.iterator().next();
// assertNotNull(report.getRecommendation());
// assertFalse(report.getRecommendation().isBlank());
// assertNotNull(report.getSummary());
// assertFalse(report.getSummary().isBlank());
// assertTrue(report.getRecommendation().toLowerCase().contains("review") ||
// report.getRecommendation().toLowerCase().contains("consult") ||
// report.getRecommendation().toLowerCase().contains("adjust"),
// "Recommendation should contain action items");

// System.out.println("LEVEL 3 PASSED - contraindicated therapy report");
// System.out.println("patient: " + report.getPatient().getName());
// System.out.println("severity: " + report.getHighestSeverity());
// System.out.println("summary: " + report.getSummary());
// System.out.println("recommendation: " + report.getRecommendation());
// }

// @Test
// @DisplayName("Level 3: Report generation - HIGH RISK therapy report")
// public void testReportGeneration_HighRiskReport() {
// PatientProfile patient = new PatientProfile(
// "Svetlana Svetkovic", 70, 65.0,
// List.of(
// Medication.builder().name("Metformin")
// .category(MedicationCategory.ANTIDIABETIC)
// .renallyCleared(true).hepaticallyMetabolized(false)
// .build(),
// Medication.builder().name("Lisinopril")
// .category(MedicationCategory.ANTIHYPERTENSIVE)
// .renallyCleared(true).hepaticallyMetabolized(false)
// .build(),
// Medication.builder().name("Simvastatin")
// .category(MedicationCategory.STATIN)
// .renallyCleared(false).hepaticallyMetabolized(true)
// .build()),
// List.of("hypertension", "diabetes"),
// new ArrayList<>(),
// new ArrayList<>());

// Medication newMedication = Medication.builder()
// .name("Ibuprofen")
// .category(MedicationCategory.OTHER)
// .renallyCleared(false)
// .hepaticallyMetabolized(true)
// .build();

// MedicationRequest request = new MedicationRequest(patient, newMedication);

// kieSession.insert(patient);
// patient.getCurrentMedications().forEach(kieSession::insert);
// kieSession.insert(newMedication);
// kieSession.insert(request);

// kieSession.fireAllRules();

// Collection<?> reports = kieSession.getObjects(obj -> obj instanceof
// SafetyReport);

// assertFalse(reports.isEmpty(), "A SafetyReport should be generated");

// SafetyReport report = (SafetyReport) reports.iterator().next();
// assertNotNull(report.getPatient());
// assertNotNull(report.getHighestSeverity());
// assertNotNull(report.getRecommendation());
// assertFalse(report.getRecommendation().isBlank());
// assertNotNull(report.getSummary());
// assertFalse(report.getSummary().isBlank());

// System.out.println("LEVEL 3 PASSED - high risk therapy report");
// System.out.println("patient: " + report.getPatient().getName());
// System.out.println("severity: " + report.getHighestSeverity());
// System.out.println("summary: " + report.getSummary());
// System.out.println("recommendation: " + report.getRecommendation());
// }

// @Test
// @DisplayName("Level 3: Report generation - SAFE therapy report")
// public void testReportGeneration_SafeReport() {
// PatientProfile patient = new PatientProfile(
// "Milos Milosevic", 40, 75.0,
// new ArrayList<>(),
// new ArrayList<>(),
// new ArrayList<>(),
// new ArrayList<>());

// Medication ibuprofen = Medication.builder()
// .name("Ibuprofen")
// .category(MedicationCategory.OTHER)
// .renallyCleared(false)
// .hepaticallyMetabolized(true)
// .build();

// MedicationRequest request = new MedicationRequest(patient, ibuprofen);

// kieSession.insert(patient);
// kieSession.insert(ibuprofen);
// kieSession.insert(request);

// kieSession.fireAllRules();

// Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof
// DetectedInteraction);
// Collection<?> reports = kieSession.getObjects(obj -> obj instanceof
// SafetyReport);

// assertTrue(interactions.isEmpty(),
// "No interactions expected for a healthy patient with no current
// medications");
// assertFalse(reports.isEmpty(), "A SafetyReport should be generated even for
// safe therapies");

// SafetyReport report = (SafetyReport) reports.iterator().next();
// assertEquals(SeverityLevel.MILD, report.getHighestSeverity());
// assertNotNull(report.getRecommendation());
// assertFalse(report.getRecommendation().isBlank());

// System.out.println("LEVEL 3 PASSED - safe therapy report");
// System.out.println("patient: " + report.getPatient().getName());
// System.out.println("severity: " + report.getHighestSeverity());
// System.out.println("summary: " + report.getSummary());
// System.out.println("recommendation: " + report.getRecommendation());
// }

// @Test
// @DisplayName("Complex scenario: Elderly patient with multiple interactions,
// all FC levels")
// public void testCompleteForwardChainingFlow_ComplexScenario() {
// System.out.println("STARTING COMPLEX SCENARIO TEST");
// // Pacijent Pacijentijevic, 68 god, dijabetes i hipertenzija, alergija na
// // penicilin
// // trenutni lijekovi: Warfarin (antikoagulans), Metformin (antidijabetik),
// // Lisinopril (antihipertenziv)
// // novi lijek: Amoxicillin (antibiotik iz grupe penicilina)
// //
// // level 1 - detektovane interakcije:
// // 1. alergija na penicilin + amoxicillin -> CONTRAINDICATED (score 10)
// // 2. antikoagulans (warfarin) + antibiotik -> SERIOUS, metformin
// renallyCleared
// // + pacijent 68 god -> CONTRAINDICATED (score 10)
// // 3. antidijabetik (metformin) + antibiotik -> SERIOUS, metformin
// // renallyCleared + pacijent 68 god -> CONTRAINDICATED (score 10)
// //
// // level 2 - agregacija: score = 30, highRisk = true
// // level 3 - report: CONTRAINDICATED

// List<Medication> existingMeds = List.of(
// Medication.builder().name("Warfarin").category(MedicationCategory.ANTICOAGULANT)
// .renallyCleared(false).hepaticallyMetabolized(true).build(),
// Medication.builder().name("Metformin").category(MedicationCategory.ANTIDIABETIC)
// .renallyCleared(true).hepaticallyMetabolized(false).build(),
// Medication.builder().name("Lisinopril").category(MedicationCategory.ANTIHYPERTENSIVE)
// .renallyCleared(true).hepaticallyMetabolized(false).build());

// PatientProfile patient = new PatientProfile(
// "Pacijent Pacijentijevic", 68, 78.0,
// new ArrayList<>(existingMeds),
// List.of("diabetes", "hypertension"),
// List.of("penicillin"),
// new ArrayList<>());

// Medication newMedication = Medication.builder()
// .name("Amoxicillin")
// .category(MedicationCategory.ANTIBIOTIC)
// .renallyCleared(true)
// .hepaticallyMetabolized(false)
// .build();

// MedicationRequest request = new MedicationRequest(patient, newMedication);

// kieSession.insert(patient);
// existingMeds.forEach(kieSession::insert);
// kieSession.insert(newMedication);
// kieSession.insert(request);

// int firedRules = kieSession.fireAllRules();

// Collection<?> interactions = kieSession.getObjects(obj -> obj instanceof
// DetectedInteraction);
// Collection<?> therapyRisks = kieSession.getObjects(obj -> obj instanceof
// TherapyRisk);
// Collection<?> reports = kieSession.getObjects(obj -> obj instanceof
// SafetyReport);

// // level 1 - interakcije su detektovane i sve imaju pozitivan score
// assertTrue(firedRules > 0);
// assertFalse(interactions.isEmpty());
// interactions.forEach(obj -> {
// DetectedInteraction di = (DetectedInteraction) obj;
// assertNotNull(di.getSeverity());
// assertNotNull(di.getInteractionType());
// assertNotNull(di.getReason());
// assertTrue(di.getScore() > 0);
// });

// // level 2 - ukupni score >= 10, terapija je visokorizicna
// assertFalse(therapyRisks.isEmpty());
// TherapyRisk risk = (TherapyRisk) therapyRisks.iterator().next();
// assertTrue(risk.getTotalScore() >= 10, "score should be >= 10, was: " +
// risk.getTotalScore());
// assertTrue(risk.isHighRisk());

// // level 3 - generisan je CONTRAINDICATED report
// assertFalse(reports.isEmpty());
// SafetyReport report = reports.stream()
// .map(r -> (SafetyReport) r)
// .filter(r -> r.getHighestSeverity() == SeverityLevel.CONTRAINDICATED)
// .findFirst()
// .orElseThrow(() -> new AssertionError("No CONTRAINDICATED report found"));
// assertNotNull(report.getRecommendation());
// assertFalse(report.getRecommendation().isBlank());

// System.out.println("COMPLEX SCENARIO PASSED");
// System.out.println("-- level 1 interactions (" + interactions.size() + ")
// --");
// interactions.forEach(i -> {
// DetectedInteraction di = (DetectedInteraction) i;
// System.out.println(" " + di.getNewMedication().getName() + " / " +
// di.getInteractionType()
// + " / " + di.getSeverity() + " (score: " + di.getScore() + ")");
// System.out.println(" reason: " + di.getReason());
// });
// System.out.println("-- level 2 risk --");
// System.out.println("total score: " + risk.getTotalScore() + ", high risk: " +
// risk.isHighRisk());
// System.out.println("-- level 3 report --");
// System.out.println("patient: " + report.getPatient().getName() + " (age "
// + report.getPatient().getAge() + ")");
// System.out.println("severity: " + report.getHighestSeverity());
// System.out.println("summary: " + report.getSummary());
// System.out.println("recommendation: " + report.getRecommendation());
// }
// }