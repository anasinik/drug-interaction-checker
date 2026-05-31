-- ── Medications ───────────────────────────────────────────────────────────────
INSERT INTO medication (id, name, category, renally_cleared, hepatically_metabolized) VALUES
    (1,  'Warfarin',      'ANTICOAGULANT',    true,  false),
    (2,  'Heparin',       'ANTICOAGULANT',    true,  false),
    (3,  'Erythromycin',  'ANTIBIOTIC',       false, true),
    (4,  'Amoxicillin',   'ANTIBIOTIC',       true,  false),
    (5,  'Metronidazole', 'ANTIBIOTIC',       false, true),
    (6,  'Simvastatin',   'STATIN',           false, true),
    (7,  'Atorvastatin',  'STATIN',           false, true),
    (8,  'Aspirin',       'OTHER',            false, false),
    (9,  'Ibuprofen',     'OTHER',            false, true),
    (10, 'Amitriptyline', 'ANTIDEPRESSANT',   false, true),
    (11, 'Sertraline',    'ANTIDEPRESSANT',   false, true),
    (12, 'Metformin',     'ANTIDIABETIC',     true,  false),
    (13, 'Amlodipine',    'ANTIHYPERTENSIVE', false, true);

-- ── Patients ──────────────────────────────────────────────────────────────────
INSERT INTO patient (id, name, age, weight_kg, jmbg) VALUES
    (1, 'Marko Marković',   68, 84.0, '1502956710023'),
    (2, 'Ana Popović',      45, 62.0, '2203978740051'),
    (3, 'Nikola Jovanović', 34, 78.0, '0607990710187'),
    (4, 'Jelena Nikolić',   72, 70.5, '1409952755034'),
    (5, 'Stefan Petrović',  58, 91.0, '2211965710099');

-- ── Patient medications ───────────────────────────────────────────────────────
-- Marko: Warfarin + Metformin
INSERT INTO patient_medications (patient_id, medication_id) VALUES (1, 1), (1, 12);
-- Ana: Atorvastatin + Amlodipine
INSERT INTO patient_medications (patient_id, medication_id) VALUES (2, 7), (2, 13);
-- Nikola: Sertraline
INSERT INTO patient_medications (patient_id, medication_id) VALUES (3, 11);
-- Jelena: Warfarin + Amlodipine + Metformin + Atorvastatin + Sertraline
INSERT INTO patient_medications (patient_id, medication_id) VALUES (4, 1), (4, 13), (4, 12), (4, 7), (4, 11);
-- Stefan: Warfarin + Atorvastatin
INSERT INTO patient_medications (patient_id, medication_id) VALUES (5, 1), (5, 7);

-- ── Diagnoses ─────────────────────────────────────────────────────────────────
INSERT INTO patient_diagnoses (patient_id, diagnosis) VALUES
    (1, 'Renal insufficiency'),
    (2, 'Hypertension'),
    (4, 'Renal insufficiency'),
    (4, 'Liver disease'),
    (5, 'Liver disease');

-- ── Allergies ─────────────────────────────────────────────────────────────────
INSERT INTO patient_allergies (patient_id, allergy) VALUES
    (2, 'Penicillin'),
    (3, 'Aspirin');

-- ── Food habits ───────────────────────────────────────────────────────────────
INSERT INTO patient_food_habits (patient_id, food_habit) VALUES
    (1, 'ALCOHOL'),
    (2, 'GRAPEFRUIT'),
    (3, 'ALCOHOL'),
    (4, 'VITAMIN_K_RICH'),
    (5, 'GRAPEFRUIT');