package com.ftn.sbnz.kjar;

import java.io.*;
import java.util.*;
import org.drools.template.ObjectDataCompiler;

public class DrugInteractionDrlGenerator {

  public static void main(String[] args) throws IOException {
    List<Map<String, Object>> data = List.of(
        row("ANTICOAGULANT", "ANTIBIOTIC", "SERIOUS", "Antibiotics reduce vitamin K-producing bacteria..."),
        row("ANTICOAGULANT", "ANTIDEPRESSANT", "MILD", "Increased risk of bleeding"),
        row("ANTIDEPRESSANT", "ANTIDEPRESSANT", "CONTRAINDICATED", "Risk of serotonin syndrome"),
        row("ANTIDIABETIC", "ANTIBIOTIC", "SERIOUS",
            "Antibiotics may enhance antidiabetic effect causing hypoglycemia"),
        row("ANTIHYPERTENSIVE", "ANTIDEPRESSANT", "MILD", "Possible drop in blood pressure"),
        row("STATIN", "ANTIBIOTIC", "SERIOUS", "Increased risk of myopathy"));

    ObjectDataCompiler compiler = new ObjectDataCompiler();

    File templateFile = new File("src/main/resources/templates/drug_drug_interaction.drt");
    try (InputStream template = new FileInputStream(templateFile)) {

      String drl = compiler.compile(data, template);

      File output = new File("src/main/resources/rules/drug_drug_interactions.drl");
      try (FileWriter fw = new FileWriter(output)) {
        fw.write(drl);
      }

      System.out.println("Generated: " + output.getAbsolutePath());
    }
  }

  private static Map<String, Object> row(String a, String b, String severity, String reason) {
    return Map.of("categoryA", a, "categoryB", b, "severity", severity, "reason", reason);
  }
}