function initials(name) {
  return name.split(" ").map((w) => w[0]).join("").slice(0, 2).toUpperCase()
}

export default function PatientCard({ patient, selected, onClick }) {
  return (
    <button
      className={`patient-card ${selected ? "patient-card--selected" : ""}`}
      onClick={onClick}
    >
      <div className="patient-card__avatar">{initials(patient.name)}</div>
      <div>
        <p className="patient-card__name">{patient.name}</p>
        <p className="patient-card__meta">{patient.age} yrs · {patient.jmbg}</p>
      </div>
    </button>
  )
}