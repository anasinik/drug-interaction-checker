-- Lijekovi (referentna tabela)
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


-- PACIJENT 1 - Lana Kovic, 40g
-- Pije: Warfarin, Metformin
-- Dijagnoza: nema
-- Navike: hrana bogata vitaminom K, alkohol
--
-- + Metronidazole -> drug-food CONTRAINDICATED: alkohol uz Metronidazole je opasna kombinacija
--                -> drug-drug SERIOUS: antibiotik pojacava dejstvo Warfarina
--              -> drug-drug SERIOUS: antidijabetik + antibiotik (Metformin vec u terapiji)

INSERT INTO patient (id, name, age, weight_kg, jmbg) VALUES
    (1, 'Lana Kovic', 40, 65.0, '1502984740011');

INSERT INTO patient_medications (patient_id, medication_id) VALUES (1, 1), (1, 12);

INSERT INTO patient_food_habits (patient_id, food_habit) VALUES
    (1, 'VITAMIN_K_RICH'),
    (1, 'ALCOHOL');


-- PACIJENT 2 - Bojana Stefanovic, 52g
-- Pije: Warfarin, Sertraline
-- Dijagnoza: oboljenje jetre
-- Navike: nema
--
-- + Atorvastatin -> drug-disease CONTRAINDICATED: statini kontraindikovani kod oboljenja jetre
-- + Heparin     -> drug-disease SERIOUS: antikoagulant rizican kod ostecene jetre
--               -> drug-drug: nema (isti tip kao Warfarin, ne postoji krizna kombinacija)

INSERT INTO patient (id, name, age, weight_kg, jmbg) VALUES
    (2, 'Bojana Stefanovic', 52, 70.0, '0803972740022');

INSERT INTO patient_medications (patient_id, medication_id) VALUES (2, 1), (2, 11);

INSERT INTO patient_diagnoses (patient_id, diagnosis) VALUES
    (2, 'Liver disease');


-- PACIJENT 3 - Dragan Ilic, 70g
-- Pije: Metformin
-- Dijagnoza: insuficijencija bubrega
-- Navike: nema
-- Napomena: pacijent ima vise od 65 godina, lijekovi koji se renalno eliminisu eskaliraju na CONTRAINDICATED
--
-- + Warfarin     -> drug-disease SERIOUS: antikoagulant + bubrezi, eskalira na CONTRAINDICATED jer je
--                   Warfarin renalno klirensan i pacijent je stariji od 65
-- + Amoxicillin  -> drug-drug SERIOUS: antibiotik + antidijabetik (Metformin vec u terapiji),
--                   eskalira na CONTRAINDICATED jer je Amoxicillin renalno klirensan i pacijent > 65

INSERT INTO patient (id, name, age, weight_kg, jmbg) VALUES
    (3, 'Dragan Ilic', 70, 80.0, '1507954710033');

INSERT INTO patient_medications (patient_id, medication_id) VALUES (3, 12);

INSERT INTO patient_diagnoses (patient_id, diagnosis) VALUES
    (3, 'Renal insufficiency');


-- PACIJENT 4 - Milica Djordjevic, 35g
-- Pije: Warfarin, Atorvastatin
-- Dijagnoza: nema
-- Alergija: penicilin
-- Navike: grejpfrut
--
-- + Amoxicillin -> special case CONTRAINDICATED: Amoxicillin je penicilin, pacijent alergican
--              -> drug-drug SERIOUS: antibiotik + antikoagulant (Warfarin)
--              -> drug-drug SERIOUS: antibiotik + statin (Atorvastatin)
-- + Aspirin    -> special case SERIOUS: Aspirin pojacava rizik od krvarenja uz antikoagulant
-- + Ibuprofen  -> special case SERIOUS: NSAID povecava antikoagulantni efekat Warfarina
-- + Amlodipine -> drug-food SERIOUS: grejpfrut inhibira metabolizam antihipertenziva

INSERT INTO patient (id, name, age, weight_kg, jmbg) VALUES
    (4, 'Milica Djordjevic', 35, 60.0, '2209989755044');

INSERT INTO patient_medications (patient_id, medication_id) VALUES (4, 1), (4, 7);

INSERT INTO patient_allergies (patient_id, allergy) VALUES (4, 'Penicillin');

INSERT INTO patient_food_habits (patient_id, food_habit) VALUES (4, 'GRAPEFRUIT');


-- PACIJENT 5 - Stefan Markovic, 45g
-- Pije: Warfarin, Amlodipine, Metformin, Sertraline, Erythromycin  (5 lijekova, polypharmacy)
-- Dijagnoza: nema
-- Navike: nema
--
-- + Simvastatin   -> special case CONTRAINDICATED: Simvastatin uz Erythromycin povecava rizik od rabdomiolize
--                 -> drug-drug SERIOUS: statin + antibiotik
-- + Amitriptyline -> drug-drug CONTRAINDICATED: dva antidepresiva zajedno (Sertraline vec u terapiji),
--                    rizik od serotonin sindroma
--                 -> drug-drug MILD: antidepresiv + antikoagulant (Warfarin)
--                 -> drug-drug MILD: antidepresiv + antihipertenziv (Amlodipine)
--                    obe MILD interakcije eskaliraju na SERIOUS zbog polypharmacy (5+ lijekova u terapiji)

INSERT INTO patient (id, name, age, weight_kg, jmbg) VALUES
    (5, 'Stefan Markovic', 45, 88.0, '1501979710055');

INSERT INTO patient_medications (patient_id, medication_id) VALUES
    (5, 1),   -- Warfarin
    (5, 13),  -- Amlodipine
    (5, 12),  -- Metformin
    (5, 11),  -- Sertraline
    (5, 3);   -- Erythromycin


-- PACIJENT 6 - Jovana Petrovic, 30g
-- Pije: Amlodipine
-- Dijagnoza: hipertenzija
-- Navike: nema
-- Napomena: kontrolni slucaj, pacijent bez znacajnih interakcija
--
-- + Amoxicillin -> nema interakcija sa Amlodipine, dijagnoza nije relevantna
--                  ocekivano: "No significant interactions detected", SafetyReport ostaje MILD

INSERT INTO patient (id, name, age, weight_kg, jmbg) VALUES
    (6, 'Jovana Petrovic', 30, 58.0, '1505996755066');

INSERT INTO patient_medications (patient_id, medication_id) VALUES (6, 13);

INSERT INTO patient_diagnoses (patient_id, diagnosis) VALUES (6, 'Hypertension');