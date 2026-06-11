const BASE = "http://localhost:8090/api"

async function get(path) {
  const res = await fetch(`${BASE}${path}`)
  if (!res.ok) throw new Error(`Server error: ${res.status}`)
  return res.json()
}

export const fetchPatients = () => get("/patients")
export const fetchPatientByJmbg = (jmbg) => get(`/patients/jmbg/${jmbg}`)
export const searchPatients = (q) => get(`/patients/search?q=${encodeURIComponent(q)}`)
export const fetchMedications = () => get("/medications")

export const checkInteractions = (patientId, medicationId) =>
  get(`/interactions/safety-report?patientId=${patientId}&medicationId=${medicationId}`)

export const getDetectedInteractions = (patientId, medicationId) =>
  get(`/interactions?patientId=${patientId}&medicationId=${medicationId}`)

export const explainContraindication = (patientId, medicationName) =>
  get(`/interactions/contraindication-explanations?patientId=${patientId}&medicationName=${encodeURIComponent(medicationName)}`)

export const explainSeverity = (factor, medicationName) =>
  get(`/interactions/severity-explanation?factor=${encodeURIComponent(factor)}&medicationName=${encodeURIComponent(medicationName)}`)