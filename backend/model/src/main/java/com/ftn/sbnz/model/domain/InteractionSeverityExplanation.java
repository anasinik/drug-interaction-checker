package com.ftn.sbnz.model.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractionSeverityExplanation {
  private String factor;
  private String medication;
  private String causalChain;
  private String finalRiskLevel;
}
