package com.ftn.sbnz.service.components;

import com.ftn.sbnz.model.domain.*;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

@Component
public class CausalFactsInitializer {

  public void insertFacts(KieSession session) {

    session.insert(new Worsens("renal insufficiency", "reduced drug clearance"));
    session.insert(new Worsens("reduced drug clearance", "drug accumulation"));
    session.insert(new Worsens("renal insufficiency", "elevated potassium"));
    session.insert(new Worsens("elevated potassium", "cardiac arrhythmia risk"));
    session.insert(new Worsens("liver disease", "reduced drug metabolism"));
    session.insert(new Worsens("reduced drug metabolism", "liver drug toxicity"));
    session.insert(new Worsens("liver disease", "reduced protein synthesis"));
    session.insert(new Worsens("reduced protein synthesis", "increased free drug fraction"));
    session.insert(new Worsens("age over 65", "reduced renal function"));
    session.insert(new Worsens("reduced renal function", "reduced drug clearance"));
    session.insert(new Worsens("cardiac arrhythmia", "prolonged qt interval"));
    session.insert(new Worsens("prolonged qt interval", "cardiac arrhythmia risk"));
    session.insert(new Worsens("hypertension", "increased vascular resistance"));
    session.insert(new Worsens("increased vascular resistance", "cardiovascular strain"));

    session.insert(new ContraindicatedWith("Simvastatin", "liver drug toxicity"));
    session.insert(new ContraindicatedWith("Atorvastatin", "liver drug toxicity"));
    session.insert(new ContraindicatedWith("Metformin", "drug accumulation"));
    session.insert(new ContraindicatedWith("Warfarin", "increased free drug fraction"));
    session.insert(new ContraindicatedWith("Heparin", "increased free drug fraction"));
    session.insert(new ContraindicatedWith("Amitriptyline", "cardiac arrhythmia risk"));
    session.insert(new ContraindicatedWith("Amitriptyline", "prolonged qt interval"));
    session.insert(new ContraindicatedWith("Amlodipine", "cardiovascular strain"));

    session.insert(new RiskMechanism("renal insufficiency", "reduced drug clearance"));
    session.insert(new RiskMechanism("reduced drug clearance", "drug accumulation"));
    session.insert(new RiskMechanism("liver disease", "reduced drug metabolism"));
    session.insert(new RiskMechanism("reduced drug metabolism", "liver drug toxicity"));
    session.insert(new RiskMechanism("liver disease", "reduced protein synthesis"));
    session.insert(new RiskMechanism("reduced protein synthesis", "increased free drug fraction"));
    session.insert(new RiskMechanism("grapefruit", "CYP3A4 inhibition"));
    session.insert(new RiskMechanism("CYP3A4 inhibition", "reduced drug metabolism"));
    session.insert(new RiskMechanism("reduced drug metabolism", "drug accumulation"));
    session.insert(new RiskMechanism("alcohol consumption", "enhanced CNS depression"));
    session.insert(new RiskMechanism("alcohol consumption", "lactic acidosis risk"));
    session.insert(new RiskMechanism("alcohol consumption", "disulfiram-like reaction"));
    session.insert(new RiskMechanism("vitamin k rich food", "reduced anticoagulant efficacy"));
    session.insert(new RiskMechanism("reduced anticoagulant efficacy", "increased clotting risk"));
    session.insert(new RiskMechanism("cardiac arrhythmia", "prolonged qt interval"));
    session.insert(new RiskMechanism("prolonged qt interval", "cardiac arrhythmia risk"));
    session.insert(new RiskMechanism("antiplatelet effect", "increased bleeding risk"));
    session.insert(new RiskMechanism("gastric irritation", "increased bleeding risk"));
    session.insert(new RiskMechanism("CYP3A4 inhibition", "simvastatin accumulation"));
    session.insert(new RiskMechanism("simvastatin accumulation", "rhabdomyolysis risk"));
    session.insert(new RiskMechanism("antiplatelet effect", "increased bleeding risk"));
    session.insert(new RiskMechanism("gastric irritation", "increased bleeding risk"));

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
    session.insert(new RiskClassification("increased bleeding risk", "SERIOUS"));
    session.insert(new RiskClassification("simvastatin accumulation", "CONTRAINDICATED"));
  }
}