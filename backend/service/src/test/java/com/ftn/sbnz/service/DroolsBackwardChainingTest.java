package com.ftn.sbnz.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.*;
import org.kie.api.KieServices;
import org.kie.api.runtime.*;
import com.ftn.sbnz.model.domain.*;
import com.ftn.sbnz.service.components.CausalFactsInitializer;

@DisplayName("Drools Backward Chaining")
public class DroolsBackwardChainingTest {

        private KieSession kieSession;
        private CausalFactsInitializer initializer;

        @BeforeEach
        public void setup() {
                KieServices ks = KieServices.Factory.get();
                KieContainer kContainer = ks.newKieContainer(
                                ks.newReleaseId("com.ftn.sbnz", "kjar", "0.0.1-SNAPSHOT"));
                kieSession = kContainer.newKieSession();
                initializer = new CausalFactsInitializer();
                initializer.insertFacts(kieSession);
        }

        @AfterEach
        public void teardown() {
                if (kieSession != null)
                        kieSession.dispose();
        }

        @Test
        @DisplayName("BC1: Metformin is contraindicated via renal insufficiency -> reduced clearance -> drug accumulation")
        public void testBC1_MetforminContraindicatedViaRenalInsufficiency() {

                Collection<?> worsens = kieSession.getObjects(obj -> obj instanceof Worsens);
                Collection<?> contraindicated = kieSession.getObjects(obj -> obj instanceof ContraindicatedWith);
                System.out.println("Worsens facts: " + worsens.size());
                System.out.println("ContraindicatedWith facts: " + contraindicated.size());

                PatientProfile patient = new PatientProfile(
                                "Marko Markovic", 68, 78.0,
                                new ArrayList<>(),
                                List.of("renal insufficiency"),
                                new ArrayList<>(),
                                new ArrayList<>());

                kieSession.insert(patient);
                kieSession.insert(new ExplanationRequest(patient, "Metformin"));
                kieSession.fireAllRules();

                Collection<?> explanations = kieSession.getObjects(
                                obj -> obj instanceof ContraindicationExplanation);

                assertFalse(explanations.isEmpty(),
                                "Should find contraindication explanation for Metformin + renal insufficiency");

                ContraindicationExplanation explanation = (ContraindicationExplanation) explanations.iterator().next();

                assertEquals("Metformin", explanation.getMedicationName());
                assertEquals("renal insufficiency", explanation.getDiagnosis());
                assertTrue(explanation.getCausalChain().contains("drug accumulation"),
                                "Chain should end at drug accumulation. Got: " + explanation.getCausalChain());

                System.out.println("BC1 PASSED - Metformin contraindicated via renal insufficiency");
                System.out.println("causal chain: " + explanation.getCausalChain());
        }

        @Test
        @DisplayName("BC1: Statin is contraindicated via causal chain liver disease -> reduced metabolism -> liver toxicity")
        public void testBC1_StatinContraindicatedViaLiverDisease() {
                PatientProfile patient = new PatientProfile(
                                "Ana Anic", 55, 65.0,
                                new ArrayList<>(),
                                List.of("liver disease"),
                                new ArrayList<>(),
                                new ArrayList<>());

                kieSession.insert(patient);
                kieSession.insert(new ExplanationRequest(patient, "Statin"));
                kieSession.fireAllRules();

                Collection<?> explanations = kieSession.getObjects(
                                obj -> obj instanceof ContraindicationExplanation);

                assertFalse(explanations.isEmpty(),
                                "Should find contraindication explanation for Statin + liver disease");

                ContraindicationExplanation explanation = (ContraindicationExplanation) explanations.iterator().next();

                assertTrue(explanation.getCausalChain().contains("liver drug toxicity"),
                                "Chain should end at liver drug toxicity. Got: " + explanation.getCausalChain());

                System.out.println("BC1 PASSED - Statin contraindicated via liver disease");
                System.out.println("causal chain: " + explanation.getCausalChain());
        }

        @Test
        @DisplayName("BC1: Metformin is NOT contraindicated for a diagnosis that does not lead to drug accumulation")
        public void testBC1_MetforminNotContraindicatedForUnrelatedDiagnosis() {
                PatientProfile patient = new PatientProfile(
                                "Pera Peric", 45, 80.0,
                                new ArrayList<>(),
                                List.of("hypertension"),
                                new ArrayList<>(),
                                new ArrayList<>());

                kieSession.insert(patient);
                kieSession.insert(new ExplanationRequest(patient, "Metformin"));
                kieSession.fireAllRules();

                Collection<?> explanations = kieSession.getObjects(
                                obj -> obj instanceof ContraindicationExplanation);

                assertTrue(explanations.isEmpty(),
                                "Should NOT find contraindication for Metformin + hypertension");

                System.out.println("BC1 PASSED - Metformin correctly NOT contraindicated for hypertension");
        }

        @Test
        @DisplayName("BC2: grapefruit + statin is serious via causal chain grapefruit -> CYP3A4 -> reduced metabolism -> drug accumulation")
        public void testBC2_GrapefruitStatinInteractionExplanation() {
                kieSession.insert(new SeverityExplanationRequest("grapefruit", "Statin"));
                kieSession.fireAllRules();

                Collection<?> explanations = kieSession.getObjects(
                                obj -> obj instanceof InteractionSeverityExplanation);

                assertFalse(explanations.isEmpty(),
                                "Should find severity explanation for grapefruit + Statin");

                InteractionSeverityExplanation explanation = (InteractionSeverityExplanation) explanations.iterator()
                                .next();

                assertTrue(explanation.getCausalChain().contains("CYP3A4 inhibition"),
                                "Chain should contain CYP3A4 inhibition. Got: " + explanation.getCausalChain());
                assertTrue(explanation.getCausalChain().contains("drug accumulation"),
                                "Chain should end at drug accumulation. Got: " + explanation.getCausalChain());
                assertEquals("SERIOUS", explanation.getFinalRiskLevel());

                System.out.println("BC2 PASSED - grapefruit + statin severity explained");
                System.out.println("causal chain: " + explanation.getCausalChain());
        }

        @Test
        @DisplayName("BC2: alcohol + antidepressant is serious via causal chain alcohol -> enhanced CNS depression")
        public void testBC2_AlcoholAntidepressantExplanation() {
                kieSession.insert(new SeverityExplanationRequest("alcohol consumption", "Antidepressant"));
                kieSession.fireAllRules();

                Collection<?> explanations = kieSession.getObjects(
                                obj -> obj instanceof InteractionSeverityExplanation);

                assertFalse(explanations.isEmpty(),
                                "Should find severity explanation for alcohol + antidepressant");

                InteractionSeverityExplanation explanation = (InteractionSeverityExplanation) explanations.iterator()
                                .next();

                assertTrue(explanation.getCausalChain().contains("enhanced CNS depression"),
                                "Chain should contain CNS depression. Got: " + explanation.getCausalChain());
                assertEquals("SERIOUS", explanation.getFinalRiskLevel());

                System.out.println("BC2 PASSED - alcohol + antidepressant severity explained");
                System.out.println("causal chain: " + explanation.getCausalChain());
        }
}