package com.ftn.sbnz.kjar;

import java.io.*;
import java.util.*;
import org.drools.template.ObjectDataCompiler;

public class RuleGenerator {

        public static void main(String[] args) throws IOException {
                generateDrugDrugInteractions();
                generateDrugDiseaseInteractions();
                generateDrugDiseaseByNameInteractions();
                generateDrugAllergyInteractions();
        }

        private static void generateDrugDrugInteractions() throws IOException {
                List<Map<String, Object>> data = List.of(
                                row2("ANTICOAGULANT", "ANTIBIOTIC", "SERIOUS",
                                                "Antibiotics reduce vitamin K-producing bacteria..."),
                                row2("ANTICOAGULANT", "ANTIDEPRESSANT", "MILD", "Increased risk of bleeding"),
                                row2("ANTIDEPRESSANT", "ANTIDEPRESSANT", "CONTRAINDICATED",
                                                "Risk of serotonin syndrome"),
                                row2("ANTIDIABETIC", "ANTIBIOTIC", "SERIOUS",
                                                "Antibiotics may enhance antidiabetic effect causing hypoglycemia"),
                                row2("ANTIHYPERTENSIVE", "ANTIDEPRESSANT", "MILD", "Possible drop in blood pressure"),
                                row2("STATIN", "ANTIBIOTIC", "SERIOUS", "Increased risk of myopathy"));
                generate("src/main/resources/templates/drug_drug_interaction.drt",
                                "src/main/resources/rules/drug_drug_interactions.drl",
                                data);
        }

        private static void generateDrugDiseaseInteractions() throws IOException {
                List<Map<String, Object>> data = List.of(
                                rowDisease("ANTICOAGULANT", "renal insufficiency", "SERIOUS",
                                                "Impaired kidney function slows drug excretion increasing bleeding risk"),
                                rowDisease("ANTIDIABETIC", "renal insufficiency", "CONTRAINDICATED",
                                                "Drug accumulation leads to hypoglycemia risk"),
                                rowDisease("STATIN", "liver disease", "CONTRAINDICATED",
                                                "Statins metabolized in liver, damaged liver causes toxicity"),
                                rowDisease("ANTICOAGULANT", "liver disease", "SERIOUS",
                                                "Damaged liver combined with anticoagulant drastically increases bleeding risk"));
                generate("src/main/resources/templates/drug_disease_interaction.drt",
                                "src/main/resources/rules/drug_disease_interactions.drl",
                                data);
        }

        private static void generateDrugDiseaseByNameInteractions() throws IOException {
                List<Map<String, Object>> data = List.of(
                                rowByName("Amitriptyline", "cardiac arrhythmia", "SERIOUS",
                                                "Tricyclic antidepressants may worsen arrhythmia"));
                generate("src/main/resources/templates/drug_by_name_disease_interaction.drt",
                                "src/main/resources/rules/drug_by_name_disease_interactions.drl",
                                data);
        }

        private static void generateDrugAllergyInteractions() throws IOException {
                List<Map<String, Object>> data = List.of(
                                rowAllergy("penicillin", "ANTIBIOTIC", "CONTRAINDICATED", "Anaphylactic shock risk"));
                generate("src/main/resources/templates/drug_allergy_interaction.drt",
                                "src/main/resources/rules/drug_allergy_interactions.drl",
                                data);
        }

        private static void generate(String templatePath, String outputPath,
                        List<Map<String, Object>> data) throws IOException {
                ObjectDataCompiler compiler = new ObjectDataCompiler();
                try (InputStream template = new FileInputStream(new File(templatePath))) {
                        String drl = compiler.compile(data, template);
                        try (FileWriter fw = new FileWriter(new File(outputPath))) {
                                fw.write(drl);
                        }
                        System.out.println("Generated: " + outputPath);
                }
        }

        private static Map<String, Object> row2(String a, String b, String severity, String reason) {
                return Map.of("categoryA", a, "categoryB", b, "severity", severity, "reason", reason);
        }

        private static Map<String, Object> rowDisease(String category, String diagnosis,
                        String severity, String reason) {
                return Map.of("medicationCategory", category, "diagnosis", diagnosis,
                                "severity", severity, "reason", reason);
        }

        private static Map<String, Object> rowByName(String name, String diagnosis,
                        String severity, String reason) {
                return Map.of("medicationName", name, "diagnosis", diagnosis,
                                "severity", severity, "reason", reason);
        }

        private static Map<String, Object> rowAllergy(String allergy, String category,
                        String severity, String reason) {
                return Map.of("allergyName", allergy, "medicationCategory", category,
                                "severity", severity, "reason", reason);
        }
}