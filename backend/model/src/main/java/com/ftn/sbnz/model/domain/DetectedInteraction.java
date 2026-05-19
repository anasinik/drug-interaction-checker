package com.ftn.sbnz.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectedInteraction {

  private PatientProfile patient;
  private Medication existingMedication;
  private Medication newMedication;
  private InteractionType interactionType;
  private SeverityLevel severity;
  private int score;
  private String reason;
}
