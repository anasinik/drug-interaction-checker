package com.ftn.sbnz.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Position;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskMechanism {
  @Position(0)
  private String cause;
  @Position(1)
  private String effect;
}