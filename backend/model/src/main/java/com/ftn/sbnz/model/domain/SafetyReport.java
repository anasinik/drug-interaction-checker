package com.ftn.sbnz.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafetyReport {

  private PatientProfile patient;
  private SeverityLevel highestSeverity;
  private String summary;
  private String recommendation;
}
