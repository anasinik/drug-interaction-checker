INSERT INTO patient (id, name, age, weight_kg) VALUES (1, 'Milan Markovic', 45, 78.5);
INSERT INTO patient (id, name, age, weight_kg) VALUES (2, 'Jelena Jovic', 31, 62.0);
INSERT INTO patient (id, name, age, weight_kg) VALUES (3, 'Nikola Petrovic', 59, 88.2);
INSERT INTO patient (id, name, age, weight_kg) VALUES (4, 'Ana Ilic', 27, 55.4);
INSERT INTO patient (id, name, age, weight_kg) VALUES (5, 'Marko Nikolic', 70, 81.1);
INSERT INTO patient (id, name, age, weight_kg) VALUES (6, 'Marija Kostic', 52, 68.9);
INSERT INTO patient (id, name, age, weight_kg) VALUES (7, 'Ivan Todorovic', 38, 74.3);
INSERT INTO patient (id, name, age, weight_kg) VALUES (8, 'Sanja Dimitrijevic', 48, 70.0);
INSERT INTO patient (id, name, age, weight_kg) VALUES (9, 'Petar Radovic', 64, 93.7);
INSERT INTO patient (id, name, age, weight_kg) VALUES (10, 'Tanja Lalic', 29, 59.2);

INSERT INTO patient_diagnoses (patient_id, diagnosis) VALUES (1, 'hypertension');
INSERT INTO patient_diagnoses (patient_id, diagnosis) VALUES (2, 'diabetes');
INSERT INTO patient_diagnoses (patient_id, diagnosis) VALUES (3, 'osteoporosis');
INSERT INTO patient_diagnoses (patient_id, diagnosis) VALUES (5, 'asthma');
INSERT INTO patient_diagnoses (patient_id, diagnosis) VALUES (7, 'hyperlipidemia');

INSERT INTO patient_allergies (patient_id, allergy) VALUES (2, 'penicillin');
INSERT INTO patient_allergies (patient_id, allergy) VALUES (4, 'pollen');
INSERT INTO patient_allergies (patient_id, allergy) VALUES (9, 'shellfish');

INSERT INTO patient_food_habits (patient_id, food_habit) VALUES (1, 'ALCOHOL');
INSERT INTO patient_food_habits (patient_id, food_habit) VALUES (3, 'GRAPEFRUIT');
INSERT INTO patient_food_habits (patient_id, food_habit) VALUES (6, 'VITAMIN_K_RICH');
INSERT INTO patient_food_habits (patient_id, food_habit) VALUES (8, 'NONE');

INSERT INTO patient_medications (patient_id, name, category, renally_cleared, hepatically_metabolized) VALUES (1, 'Aspirin', 'OTHER', FALSE, TRUE);
INSERT INTO patient_medications (patient_id, name, category, renally_cleared, hepatically_metabolized) VALUES (2, 'Metformin', 'ANTIDIABETIC', TRUE, FALSE);
INSERT INTO patient_medications (patient_id, name, category, renally_cleared, hepatically_metabolized) VALUES (3, 'Omeprazole', 'OTHER', FALSE, TRUE);
INSERT INTO patient_medications (patient_id, name, category, renally_cleared, hepatically_metabolized) VALUES (5, 'Salbutamol', 'OTHER', FALSE, FALSE);
INSERT INTO patient_medications (patient_id, name, category, renally_cleared, hepatically_metabolized) VALUES (7, 'Atorvastatin', 'STATIN', TRUE, FALSE);