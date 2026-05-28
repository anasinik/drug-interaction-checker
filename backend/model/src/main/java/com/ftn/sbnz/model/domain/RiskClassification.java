package com.ftn.sbnz.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskClassification {
  private String condition;
  private String riskLevel; // "SERIOUS" or "CONTRAINDICATED"
}
