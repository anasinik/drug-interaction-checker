package com.ftn.sbnz.model.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Medication {

  private String name;

  @Enumerated(EnumType.STRING)
  private MedicationCategory category;

  private boolean renallyCleared;
  private boolean hepaticallyMetabolized;

}
