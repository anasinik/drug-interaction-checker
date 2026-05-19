package com.ftn.sbnz.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TherapyRisk {

  private PatientProfile patient;
  private int totalScore;
  private boolean highRisk;
}
