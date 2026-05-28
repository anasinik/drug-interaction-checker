package com.ftn.sbnz.model.domain;

import lombok.*;
import org.kie.api.definition.type.Position;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Worsens {
  @Position(0)
  private String cause;
  @Position(1)
  private String effect;
}