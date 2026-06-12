package com.ftn.sbnz.service.components;

import com.ftn.sbnz.model.domain.*;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

@Component
public class CausalFactsInitializer {

  public void insertFacts(KieSession session) {

    // ═════════════════════════════════════════════════════════════════
    // RISK MECHANISMS — za explainSeverity query (Why? dugme na interakciji)
    //
    // Svaki factor koji se pojavljuje kao DetectedInteraction.factor
    // mora imati lanac koji završava u RiskClassification.
    //
    // Faktori iz pravila:
    // drug-drug (generisani):
    // "anticoagulant with antibiotic" → SERIOUS
    // "anticoagulant with antidepressant" → MILD (dodajemo klasifikaciju)
    // "ANTIDEPRESSANT" → CONTRAINDICATED
    // "antidiabetic with antibiotic" → SERIOUS
    // "antihypertensive with antidepressant" → MILD
    // "STATIN" → SERIOUS
    // drug-drug (specijalni slučajevi):
    // "antiplatelet effect" → SERIOUS ✓ postoji
    // "gastric irritation" → SERIOUS ✓ postoji
    // "CYP3A4 inhibition" → CONTRAINDICATED ✓ postoji
    // "anticoagulant overlap" → CONTRAINDICATED (novo)
    // drug-disease (generisani):
    // "renal insufficiency" → SERIOUS / CONTRAINDICATED ✓ postoji
    // "liver disease" → SERIOUS / CONTRAINDICATED
    // "anticoagulant with liver disease" → SERIOUS (novo, specifičan)
    // drug-disease (specijalni slučajevi):
    // "cardiac arrhythmia" → SERIOUS ✓ postoji
    // "penicillin" → CONTRAINDICATED (novo)
    // drug-food (generisani):
    // "vitamin k rich food" → SERIOUS ✓ postoji
    // "grapefruit" → SERIOUS ✓ postoji (via CYP3A4)
    // "alcohol consumption" → SERIOUS/CONTRAINDICATED ✓ postoji
    // ═════════════════════════════════════════════════════════════════

    // ── Drug-drug: ANTICOAGULANT + ANTIBIOTIC (factor: "anticoagulant with
    // antibiotic")
    // Antibiotici uništavaju bakterije koje sintetišu vitamin K →
    // smanjuje se produkcija faktora koagulacije → pojačan efekat antikoagulanta →
    // krvarenje
    session.insert(new RiskMechanism("anticoagulant with antibiotic", "reduced vitamin K synthesis"));
    session.insert(new RiskMechanism("reduced vitamin K synthesis", "impaired clotting factor production"));
    session.insert(new RiskMechanism("impaired clotting factor production", "enhanced anticoagulant effect"));
    session.insert(new RiskMechanism("enhanced anticoagulant effect", "increased bleeding risk"));

    // ── Drug-drug: ANTICOAGULANT + ANTIDEPRESSANT (factor: "anticoagulant with
    // antidepressant")
    // SSRI inhibiraju agregaciju trombocita → pojačan antikoagulantni efekat →
    // krvarenje
    session.insert(new RiskMechanism("anticoagulant with antidepressant", "SSRI platelet inhibition"));
    session.insert(new RiskMechanism("SSRI platelet inhibition", "increased bleeding risk"));

    // ── Drug-drug: ANTIDEPRESSANT + ANTIDEPRESSANT (factor: "ANTIDEPRESSANT")
    // Dva serotonergička leka → višak serotonina → serotonin sindrom
    session.insert(new RiskMechanism("ANTIDEPRESSANT", "excess serotonin accumulation"));
    session.insert(new RiskMechanism("excess serotonin accumulation", "serotonin syndrome"));

    // ── Drug-drug: ANTIDIABETIC + ANTIBIOTIC (factor: "antidiabetic with
    // antibiotic")
    // Antibiotici inhibiraju metabolizam antidijabetika →
    // povišena koncentracija leka → pojačan hipoglikemijski efekat
    session.insert(new RiskMechanism("antidiabetic with antibiotic", "impaired antidiabetic metabolism"));
    session.insert(new RiskMechanism("impaired antidiabetic metabolism", "elevated antidiabetic concentration"));
    session.insert(new RiskMechanism("elevated antidiabetic concentration", "severe hypoglycemia risk"));

    // ── Drug-drug: ANTIHYPERTENSIVE + ANTIDEPRESSANT (factor: "antihypertensive
    // with antidepressant")
    // Antidepresivi pojačavaju antihipertenzivni efekat → nagli pad pritiska
    session.insert(new RiskMechanism("antihypertensive with antidepressant", "additive blood pressure reduction"));

    // ── Drug-drug: STATIN + ANTIBIOTIC (factor: "STATIN")
    // Određeni antibiotici inhibiraju CYP enzime → akumulacija statina → miopatija
    session.insert(new RiskMechanism("STATIN", "CYP enzyme inhibition by antibiotic"));
    session.insert(new RiskMechanism("CYP enzyme inhibition by antibiotic", "statin accumulation"));
    session.insert(new RiskMechanism("statin accumulation", "myopathy risk"));

    // ── Drug-drug specijalni: dva antikoagulanta (factor: "anticoagulant overlap")
    // Kombinacija dva antikoagulanta drastično povećava rizik od nekontrolisanog
    // krvarenja
    session.insert(new RiskMechanism("anticoagulant overlap", "combined anticoagulation effect"));
    session.insert(new RiskMechanism("combined anticoagulation effect", "uncontrolled bleeding risk"));

    // ── Drug-drug specijalni: Aspirin + antikoagulant (factor: "antiplatelet
    // effect") ✓
    session.insert(new RiskMechanism("antiplatelet effect", "increased bleeding risk"));

    // ── Drug-drug specijalni: Ibuprofen + antikoagulant (factor: "gastric
    // irritation") ✓
    session.insert(new RiskMechanism("gastric irritation", "increased bleeding risk"));

    // ── Drug-drug specijalni: Simvastatin + Erythromycin (factor: "CYP3A4
    // inhibition") ✓
    session.insert(new RiskMechanism("CYP3A4 inhibition", "simvastatin accumulation"));
    session.insert(new RiskMechanism("simvastatin accumulation", "rhabdomyolysis risk"));
    session.insert(new RiskMechanism("CYP3A4 inhibition", "reduced drug metabolism"));
    session.insert(new RiskMechanism("reduced drug metabolism", "drug accumulation"));

    // ── Drug-disease: ANTICOAGULANT + liver disease (factor: "anticoagulant with
    // liver disease")
    // Oštećena jetra smanjuje sintezu proteina → više slobodnog leka u krvi → rizik
    // od krvarenja
    // Poseban factor da ne bi lanac odlazio ka liver drug toxicity (koji važi za
    // statine)
    session.insert(new RiskMechanism("anticoagulant with liver disease", "reduced protein synthesis"));
    session.insert(new RiskMechanism("reduced protein synthesis", "increased free drug fraction"));

    // ── Drug-disease: ANTICOAGULANT + renal insufficiency (factor: "renal
    // insufficiency") ✓
    // ── Drug-disease: ANTIDIABETIC + renal insufficiency (factor: "renal
    // insufficiency") ✓
    // ── Drug-disease: STATIN + liver disease (factor: "liver disease") ✓
    // Ovi koriste već postojeće lance ispod

    // ── Drug-disease: dijagnoza → mehanizam lanci ✓
    session.insert(new RiskMechanism("renal insufficiency", "reduced drug clearance"));
    session.insert(new RiskMechanism("reduced drug clearance", "drug accumulation"));
    session.insert(new RiskMechanism("liver disease", "reduced drug metabolism"));
    session.insert(new RiskMechanism("reduced drug metabolism", "liver drug toxicity"));
    session.insert(new RiskMechanism("liver disease", "reduced protein synthesis"));

    // ── Drug-disease specijalni: Amitriptyline + cardiac arrhythmia (factor:
    // "cardiac arrhythmia") ✓
    session.insert(new RiskMechanism("cardiac arrhythmia", "prolonged qt interval"));
    session.insert(new RiskMechanism("prolonged qt interval", "cardiac arrhythmia risk"));

    // ── Drug-allergy: penicillin + ANTIBIOTIC (factor: "penicillin")
    // Izloženost alergenu → IgE-posredovana reakcija → anafilaktički šok
    session.insert(new RiskMechanism("penicillin", "immune system allergen recognition"));
    session.insert(new RiskMechanism("immune system allergen recognition", "IgE-mediated hypersensitivity"));
    session.insert(new RiskMechanism("IgE-mediated hypersensitivity", "anaphylactic shock risk"));

    // ── Drug-food: vitamin K (factor: "vitamin k rich food") ✓
    session.insert(new RiskMechanism("vitamin k rich food", "reduced anticoagulant efficacy"));
    session.insert(new RiskMechanism("reduced anticoagulant efficacy", "increased clotting risk"));

    // ── Drug-food: grapefruit (factor: "grapefruit") ✓
    session.insert(new RiskMechanism("grapefruit", "CYP3A4 inhibition"));

    // ── Drug-food: alcohol (factor: "alcohol consumption") ✓
    session.insert(new RiskMechanism("alcohol consumption", "enhanced CNS depression"));
    session.insert(new RiskMechanism("alcohol consumption", "lactic acidosis risk"));
    session.insert(new RiskMechanism("alcohol consumption", "disulfiram-like reaction"));

    // ═════════════════════════════════════════════════════════════════
    // RISK CLASSIFICATIONS — krajnji efekti sa ocenom ozbiljnosti
    // ═════════════════════════════════════════════════════════════════

    session.insert(new RiskClassification("drug accumulation", "SERIOUS"));
    session.insert(new RiskClassification("liver drug toxicity", "CONTRAINDICATED"));
    session.insert(new RiskClassification("increased free drug fraction", "SERIOUS"));
    session.insert(new RiskClassification("cardiac arrhythmia risk", "SERIOUS"));
    session.insert(new RiskClassification("enhanced CNS depression", "SERIOUS"));
    session.insert(new RiskClassification("lactic acidosis risk", "SERIOUS"));
    session.insert(new RiskClassification("disulfiram-like reaction", "CONTRAINDICATED"));
    session.insert(new RiskClassification("increased clotting risk", "SERIOUS"));
    session.insert(new RiskClassification("rhabdomyolysis risk", "CONTRAINDICATED"));
    session.insert(new RiskClassification("increased bleeding risk", "SERIOUS"));
    session.insert(new RiskClassification("prolonged qt interval", "SERIOUS"));
    session.insert(new RiskClassification("cardiovascular strain", "SERIOUS"));
    session.insert(new RiskClassification("simvastatin accumulation", "CONTRAINDICATED"));
    // novi
    session.insert(new RiskClassification("enhanced anticoagulant effect", "SERIOUS"));
    session.insert(new RiskClassification("serotonin syndrome", "CONTRAINDICATED"));
    session.insert(new RiskClassification("severe hypoglycemia risk", "SERIOUS"));
    session.insert(new RiskClassification("myopathy risk", "SERIOUS"));
    session.insert(new RiskClassification("anaphylactic shock risk", "CONTRAINDICATED"));
    session.insert(new RiskClassification("uncontrolled bleeding risk", "CONTRAINDICATED"));
    session.insert(new RiskClassification("additive blood pressure reduction", "MILD"));
  }
}