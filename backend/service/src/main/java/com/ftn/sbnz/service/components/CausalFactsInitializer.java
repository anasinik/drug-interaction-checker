package com.ftn.sbnz.service.components;

import com.ftn.sbnz.model.domain.*;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

@Component
public class CausalFactsInitializer {

  public void insertFacts(KieSession session) {

    session.insert(new RiskMechanism("anticoagulant with antibiotic", "reduced vitamin K synthesis"));
    session.insert(new RiskMechanism("reduced vitamin K synthesis", "impaired clotting factor production"));
    session.insert(new RiskMechanism("impaired clotting factor production", "enhanced anticoagulant effect"));
    session.insert(new RiskMechanism("enhanced anticoagulant effect", "increased bleeding risk"));
    session.insert(new RiskMechanism("anticoagulant with antidepressant", "SSRI platelet inhibition"));
    session.insert(new RiskMechanism("SSRI platelet inhibition", "increased bleeding risk"));
    session.insert(new RiskMechanism("ANTIDEPRESSANT", "excess serotonin accumulation"));
    session.insert(new RiskMechanism("excess serotonin accumulation", "serotonin syndrome"));
    session.insert(new RiskMechanism("antidiabetic with antibiotic", "impaired antidiabetic metabolism"));
    session.insert(new RiskMechanism("impaired antidiabetic metabolism", "elevated antidiabetic concentration"));
    session.insert(new RiskMechanism("elevated antidiabetic concentration", "severe hypoglycemia risk"));
    session.insert(new RiskMechanism("antihypertensive with antidepressant", "additive blood pressure reduction"));
    session.insert(new RiskMechanism("STATIN", "CYP enzyme inhibition by antibiotic"));
    session.insert(new RiskMechanism("CYP enzyme inhibition by antibiotic", "statin accumulation"));
    session.insert(new RiskMechanism("statin accumulation", "myopathy risk"));
    session.insert(new RiskMechanism("anticoagulant overlap", "combined anticoagulation effect"));
    session.insert(new RiskMechanism("combined anticoagulation effect", "uncontrolled bleeding risk"));
    session.insert(new RiskMechanism("antiplatelet effect", "increased bleeding risk"));
    session.insert(new RiskMechanism("gastric irritation", "increased bleeding risk"));
    session.insert(new RiskMechanism("CYP3A4 inhibition", "simvastatin accumulation"));
    session.insert(new RiskMechanism("simvastatin accumulation", "rhabdomyolysis risk"));
    session.insert(new RiskMechanism("CYP3A4 inhibition", "reduced drug metabolism"));
    session.insert(new RiskMechanism("reduced drug metabolism", "drug accumulation"));
    session.insert(new RiskMechanism("anticoagulant with liver disease", "reduced protein synthesis"));
    session.insert(new RiskMechanism("reduced protein synthesis", "increased free drug fraction"));
    session.insert(new RiskMechanism("renal insufficiency", "reduced drug clearance"));
    session.insert(new RiskMechanism("reduced drug clearance", "drug accumulation"));
    session.insert(new RiskMechanism("liver disease", "reduced drug metabolism"));
    session.insert(new RiskMechanism("reduced drug metabolism", "liver drug toxicity"));
    session.insert(new RiskMechanism("liver disease", "reduced protein synthesis"));
    session.insert(new RiskMechanism("cardiac arrhythmia", "prolonged qt interval"));
    session.insert(new RiskMechanism("prolonged qt interval", "cardiac arrhythmia risk"));
    session.insert(new RiskMechanism("penicillin", "immune system allergen recognition"));
    session.insert(new RiskMechanism("immune system allergen recognition", "IgE-mediated hypersensitivity"));
    session.insert(new RiskMechanism("IgE-mediated hypersensitivity", "anaphylactic shock risk"));
    session.insert(new RiskMechanism("vitamin k rich food", "reduced anticoagulant efficacy"));
    session.insert(new RiskMechanism("reduced anticoagulant efficacy", "increased clotting risk"));
    session.insert(new RiskMechanism("grapefruit", "CYP3A4 inhibition"));
    session.insert(new RiskMechanism("alcohol consumption", "enhanced CNS depression"));
    session.insert(new RiskMechanism("alcohol consumption", "lactic acidosis risk"));
    session.insert(new RiskMechanism("alcohol consumption", "disulfiram-like reaction"));

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
    session.insert(new RiskClassification("enhanced anticoagulant effect", "SERIOUS"));
    session.insert(new RiskClassification("serotonin syndrome", "CONTRAINDICATED"));
    session.insert(new RiskClassification("severe hypoglycemia risk", "SERIOUS"));
    session.insert(new RiskClassification("myopathy risk", "SERIOUS"));
    session.insert(new RiskClassification("anaphylactic shock risk", "CONTRAINDICATED"));
    session.insert(new RiskClassification("uncontrolled bleeding risk", "CONTRAINDICATED"));
    session.insert(new RiskClassification("additive blood pressure reduction", "MILD"));
  }
}