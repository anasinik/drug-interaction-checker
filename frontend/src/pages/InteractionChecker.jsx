import { useState, useEffect } from "react"
import * as api from "../api"

import PatientCard from "../components/PatientCard"
import PatientDetail from "../components/PatientDetail"
import SafetyReport from "../components/SafetyReport"
import InteractionList from "../components/InteractionList"
import ContraindicationExplanation from "../components/ContraindicationExplanation"
import SeverityExplanation from "../components/SeverityExplanation"

export default function InteractionChecker() {
  const [patients, setPatients] = useState([])
  const [patientsLoading, setPatientsLoading] = useState(true)
  const [patientsError, setPatientsError] = useState(null)

  const [medications, setMedications] = useState([])
  const [selectedMedId, setSelectedMedId] = useState("")

  const [search, setSearch] = useState("")
  const [selectedPatient, setSelectedPatient] = useState(null)

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const [safetyReport, setSafetyReport] = useState(null)
  const [interactions, setInteractions] = useState(null)

  const [explanations, setExplanations] = useState(null)
  const [explanationMed, setExplanationMed] = useState(null)

  const [severityExplanations, setSeverityExplanations] = useState(null)
  const [severityExplanationTarget, setSeverityExplanationTarget] = useState(null)

  async function handleExplainSeverity(factor, medicationName) {
    setSeverityExplanationTarget({ factor, medicationName })
    setSeverityExplanations(null)
    try {
      const result = await api.explainSeverity(factor, medicationName)
      console.log("severity explanations:", result)
      setSeverityExplanations(result)
    } catch {
      setSeverityExplanations([])
    }
  }

  useEffect(() => {
    api.fetchPatients()
      .then(setPatients)
      .catch(() => setPatientsError("Could not load patients."))
      .finally(() => setPatientsLoading(false))
  }, [])

  useEffect(() => {
    api.fetchMedications().then(setMedications)
  }, [])

  const filteredPatients = search.trim()
    ? patients.filter(
        (p) =>
          p.jmbg.includes(search.trim()) ||
          p.name.toLowerCase().includes(search.toLowerCase())
      )
    : patients

  const selectedMed = medications.find(m => m.id === Number(selectedMedId)) ?? null

  function resetResults() {
    setSafetyReport(null)
    setInteractions(null)
    setExplanations(null)
    setExplanationMed(null)
    setError(null)
    setSeverityExplanations(null)
    setSeverityExplanationTarget(null)
  }

  async function handleCheck() {
    if (!selectedPatient || !selectedMedId) return
    setLoading(true)
    resetResults()
    try {
      const [report, detected] = await Promise.all([
        api.checkInteractions(selectedPatient.id, selectedMedId),
        api.getDetectedInteractions(selectedPatient.id, selectedMedId),
      ])



      const seen = new Set()
      const deduplicated = detected.filter(i => {
        const key = `${i.newMedication?.id}-${i.existingMedication?.id ?? "null"}-${i.interactionType}-${i.severity}`
        if (seen.has(key)) return false
        seen.add(key)
        return true
      })


      setSafetyReport(report)
      setInteractions(deduplicated)

    } catch {
      setError("Could not connect to the server. Make sure the backend is running.")
    } finally {
      setLoading(false)
    }
  }

  async function handleExplain(medName) {
    if (!selectedPatient) return
    setExplanationMed(medName)
    setExplanations(null)
    try {
      const result = await api.explainContraindication(selectedPatient.id, medName)
      setExplanations(result)
    } catch {
      setExplanations([])
    }
  }

  const canCheck = !!selectedPatient && !!selectedMedId && !loading
  const hasContraindication = interactions?.some((i) => i.severity === "CONTRAINDICATED") ?? false

  return (
    <div className="page">
      <div className="page__header">
        <p className="eyebrow">Clinical system</p>
        <h1 className="page__title">Therapy Safety Check</h1>
      </div>

      <div className="layout-grid">
        {/* ── Left column ── */}
        <div className="left-col">

          {/* Patient selection */}
          <div className="panel">
            <p className="section-label">Patient</p>
            <input
              className="input"
              type="text"
              placeholder="Search by name or JMBG..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <div className="patient-list">
              {patientsLoading && <p className="text-muted">Loading...</p>}
              {patientsError  && <p className="text-muted">{patientsError}</p>}
              {!patientsLoading && filteredPatients.length === 0 && (
                <p className="text-muted">No results.</p>
              )}
              {filteredPatients.map((p) => (
                <PatientCard
                  key={p.id}
                  patient={p}
                  selected={selectedPatient?.id === p.id}
                  onClick={() => { setSelectedPatient(p); resetResults() }}
                />
              ))}
            </div>
          </div>

          {/* Medication picker */}
          <div className="panel">
            <p className="section-label">New medication</p>
            <select
              className="input"
              value={selectedMedId}
              onChange={(e) => setSelectedMedId(e.target.value)}
            >
              <option value="">— Select medication —</option>
              {medications.map((m) => (
                <option key={m.id} value={m.id}>{m.name}</option>
              ))}
            </select>

            <button className="btn btn--primary" onClick={handleCheck} disabled={!canCheck}>
              {loading ? "Checking..." : "Check interactions"}
            </button>
          </div>
        </div>

        {/* ── Right column ── */}
        <div className="right-col">
          {selectedPatient && <PatientDetail patient={selectedPatient} />}

          {error   && <div className="alert alert--danger">{error}</div>}
          {loading && <div className="alert alert--neutral">Checking interactions...</div>}

          {safetyReport && !loading && <SafetyReport report={safetyReport} />}

          {interactions && !loading && interactions.length === 0 && (
            <div className="alert alert--success">No interactions detected.</div>
          )}

          {interactions && !loading && interactions.length > 0 && (
            <div className="panel" style={{ position: "relative" }}>
              <div className="panel__header">
                <p className="section-label" style={{ margin: 0 }}>Detected interactions</p>
                <span className="tag">{interactions.length}</span>
              </div>

              <InteractionList interactions={interactions} onExplain={handleExplainSeverity} />

              {hasContraindication && selectedMed && (
                <button
                  className="btn btn--ghost btn--full"
                  style={{ marginTop: 8 }}
                  onClick={() => handleExplain(selectedMed.name)}
                >
                  Explain why {selectedMed.name} is contraindicated?
                </button>
              )}

              {severityExplanations !== null && severityExplanationTarget && (
                <SeverityExplanation
                  factor={severityExplanationTarget.factor}
                  medicationName={severityExplanationTarget.medicationName}
                  explanations={severityExplanations}
                  onClose={() => { setSeverityExplanations(null); setSeverityExplanationTarget(null) }}
                />
              )}

              {explanations !== null && explanationMed && (
                <ContraindicationExplanation
                  medicationName={explanationMed}
                  explanations={explanations}
                  onClose={() => { setExplanations(null); setExplanationMed(null) }}
                />
              )}
            </div>
          )}

          {!selectedPatient && !safetyReport && !loading && (
            <div className="empty-state">
              Select a patient and a medication to run the check.
            </div>
          )}
        </div>
      </div>
    </div>
  )
}