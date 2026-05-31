package com.ftn.sbnz.service.dtos;

import lombok.Data;

@Data
public class InteractionRequestDto {
  private Long patientId;
  private Long medicationId;
}