package com.ftn.sbnz.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationRequest {

  private PatientProfile patient;
  private Medication newMedication;
}
