package com.ftn.sbnz.service.components;

import com.ftn.sbnz.model.domain.*;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

@Component
public class CausalFactsInitializer {

  public void insertFacts(KieSession session) {

    // BC1 — kauzalni lanci za kontraindikacije (Worsens)
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

    session.insert(new Worsens("grapefruit", "CYP3A4 inhibition"));
    session.insert(new Worsens("CYP3A4 inhibition", "reduced drug metabolism"));
    session.insert(new Worsens("alcohol consumption", "enhanced CNS depression"));

    // Direktne kontraindikacije — krajevi lanaca (ContraindicatedWith)
    session.insert(new ContraindicatedWith("Metformin", "drug accumulation"));
    session.insert(new ContraindicatedWith("Statin", "liver drug toxicity"));
    session.insert(new ContraindicatedWith("ACE inhibitor", "cardiac arrhythmia risk"));
    session.insert(new ContraindicatedWith("Warfarin", "increased free drug fraction"));

    // BC2 — mehanizmi rizika za objašnjenje ozbiljnosti (RiskMechanism)
    session.insert(new RiskMechanism("renal insufficiency", "reduced drug clearance"));
    session.insert(new RiskMechanism("reduced drug clearance", "drug accumulation"));
    session.insert(new RiskMechanism("liver disease", "reduced drug metabolism"));
    session.insert(new RiskMechanism("reduced drug metabolism", "liver drug toxicity"));
    session.insert(new RiskMechanism("liver disease", "reduced protein synthesis"));
    session.insert(new RiskMechanism("reduced protein synthesis", "increased free drug fraction"));
    session.insert(new RiskMechanism("grapefruit", "CYP3A4 inhibition"));
    session.insert(new RiskMechanism("CYP3A4 inhibition", "reduced drug metabolism"));
    session.insert(new RiskMechanism("alcohol consumption", "enhanced CNS depression"));
    session.insert(new RiskMechanism("reduced drug metabolism", "drug accumulation"));

    // Klasifikacija nivoa rizika krajnjih stanja (RiskClassification)
    session.insert(new RiskClassification("drug accumulation", "SERIOUS"));
    session.insert(new RiskClassification("liver drug toxicity", "CONTRAINDICATED"));
    session.insert(new RiskClassification("enhanced CNS depression", "SERIOUS"));
    session.insert(new RiskClassification("cardiac arrhythmia risk", "SERIOUS"));
    session.insert(new RiskClassification("increased free drug fraction", "SERIOUS"));
  }
}