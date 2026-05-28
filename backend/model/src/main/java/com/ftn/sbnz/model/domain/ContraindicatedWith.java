package com.ftn.sbnz.model.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContraindicatedWith {
  private String medicationName;
  private String condition;
}