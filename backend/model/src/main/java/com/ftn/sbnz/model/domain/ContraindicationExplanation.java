package com.ftn.sbnz.model.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContraindicationExplanation {
  private String medicationName;
  private String diagnosis;
  private String causalChain;
}