package com.ftn.sbnz.model.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "patient")
public class PatientProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private int age;
  private double weightKg;

  @ElementCollection
  @CollectionTable(name = "patient_medications", joinColumns = @JoinColumn(name = "patient_id"))
  private List<Medication> currentMedications;

  @ElementCollection
  @CollectionTable(name = "patient_diagnoses", joinColumns = @JoinColumn(name = "patient_id"))
  @Column(name = "diagnosis")
  private List<String> diagnoses;

  @ElementCollection
  @CollectionTable(name = "patient_allergies", joinColumns = @JoinColumn(name = "patient_id"))
  @Column(name = "allergy")
  private List<String> allergies;

  @ElementCollection
  @CollectionTable(name = "patient_food_habits", joinColumns = @JoinColumn(name = "patient_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "food_habit")
  private List<FoodHabit> foodHabits;

  public PatientProfile() {
    this.currentMedications = new ArrayList<>();
    this.diagnoses = new ArrayList<>();
    this.allergies = new ArrayList<>();
    this.foodHabits = new ArrayList<>();
  }

  public PatientProfile(String name, int age, double weightKg, List<Medication> currentMedications,
      List<String> diagnoses,
      List<String> allergies, List<FoodHabit> foodHabits) {
    this.name = name;
    this.age = age;
    this.weightKg = weightKg;
    this.currentMedications = currentMedications != null ? currentMedications : new ArrayList<>();
    this.diagnoses = diagnoses != null ? diagnoses : new ArrayList<>();
    this.allergies = allergies != null ? allergies : new ArrayList<>();
    this.foodHabits = foodHabits != null ? foodHabits : new ArrayList<>();
  }

  public void setCurrentMedications(List<Medication> currentMedications) {
    this.currentMedications = currentMedications != null ? currentMedications : new ArrayList<>();
  }

  public void setDiagnoses(List<String> diagnoses) {
    this.diagnoses = diagnoses != null ? diagnoses : new ArrayList<>();
  }

  public void setAllergies(List<String> allergies) {
    this.allergies = allergies != null ? allergies : new ArrayList<>();
  }

  public void setFoodHabits(List<FoodHabit> foodHabits) {
    this.foodHabits = foodHabits != null ? foodHabits : new ArrayList<>();
  }

  public int getMedicationCount() {
    return currentMedications == null ? 0 : currentMedications.size();
  }

  public boolean hasPolypharmacy() {
    return getMedicationCount() >= ClinicalConstants.POLYPHARMACY_THRESHOLD;
  }

  public boolean hasMedicationByCategory(MedicationCategory category) {
    if (currentMedications == null || category == null)
      return false;

    return currentMedications.stream()
        .anyMatch(med -> med.getCategory() == category);
  }

  public Medication findMedicationByCategory(MedicationCategory category) {
    if (currentMedications == null || category == null)
      return null;

    return currentMedications.stream()
        .filter(med -> med.getCategory() == category)
        .findFirst()
        .orElse(null);
  }

  public boolean hasDiagnosis(String diagnosis) {
    return diagnoses != null && diagnoses.stream()
        .anyMatch(d -> d.equalsIgnoreCase(diagnosis));
  }

  public boolean hasAllergy(String allergy) {
    return allergies != null && allergies.stream()
        .anyMatch(a -> a.equalsIgnoreCase(allergy));
  }

  public boolean hasFoodHabit(FoodHabit habit) {
    return foodHabits != null && foodHabits.contains(habit);
  }

  public void addMedication(Medication medication) {
    if (medication == null)
      return;

    if (currentMedications == null)
      currentMedications = new ArrayList<>();

    currentMedications.add(medication);
  }

  public void addDiagnosis(String diagnosis) {
    if (diagnosis == null || diagnosis.isBlank())
      return;

    if (diagnoses == null)
      diagnoses = new ArrayList<>();

    diagnoses.add(diagnosis);
  }

  public void addAllergy(String allergy) {
    if (allergy == null || allergy.isBlank())
      return;

    if (allergies == null)
      allergies = new ArrayList<>();

    allergies.add(allergy);
  }

  public void addFoodHabit(FoodHabit habit) {
    if (habit == null)
      return;
    if (foodHabits == null)
      foodHabits = new ArrayList<>();
    if (!foodHabits.contains(habit))
      foodHabits.add(habit);
  }

}
