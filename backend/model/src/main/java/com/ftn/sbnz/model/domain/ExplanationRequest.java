package com.ftn.sbnz.model.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExplanationRequest {
  private PatientProfile patient; // TODO: maybe just patient ID
  private String medicationName;
}