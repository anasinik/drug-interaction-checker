package com.ftn.sbnz.model.domain;

public enum SeverityLevel {
  MILD(1),
  SERIOUS(3),
  CONTRAINDICATED(10);

  private final int score;

  SeverityLevel(int score) {
    this.score = score;
  }

  public int getScore() {
    return score;
  }
}
