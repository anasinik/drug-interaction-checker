import { FOOD_HABIT_LABEL, CATEGORY_LABEL } from "../constants"

function initials(name) {
  return name.split(" ").map((w) => w[0]).join("").slice(0, 2).toUpperCase()
}

function Row({ label, value }) {
  return (
    <div className="detail-row">
      <span className="detail-row__label">{label}</span>
      <span className="detail-row__value">{value || "—"}</span>
    </div>
  )
}

export default function PatientDetail({ patient }) {
  const medications = patient.currentMedications
    .map((m) => `${m.name} (${CATEGORY_LABEL[m.category] ?? m.category})`)
    .join(", ") || "—"

  const foodHabits = patient.foodHabits
    .map((h) => FOOD_HABIT_LABEL[h] ?? h)
    .join(", ") || "—"

  return (
    <div className="panel">
      <div className="patient-detail__header">
        <div className="patient-detail__avatar">{initials(patient.name)}</div>
        <div>
          <p className="patient-detail__name">{patient.name}</p>
          <p className="patient-detail__jmbg">JMBG: {patient.jmbg}</p>
        </div>
      </div>
      <Row label="Age / Weight"        value={`${patient.age} yrs / ${patient.weightKg} kg`} />
      <Row label="Diagnoses"           value={patient.diagnoses.join(", ")} />
      <Row label="Allergies"           value={patient.allergies.join(", ")} />
      <Row label="Food habits"         value={foodHabits} />
      <Row label="Current medications" value={medications} />
    </div>
  )
}