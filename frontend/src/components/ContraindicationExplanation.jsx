export default function ContraindicationExplanation({ medicationName, explanations, onClose }) {
  return (
    <div className="explanation-overlay">
      <div className="explanation-overlay__header">
        <div>
          <p className="eyebrow">Contraindication explanation</p>
          <h3 className="explanation-overlay__title">{medicationName}</h3>
        </div>
        <button className="btn btn--ghost" onClick={onClose}>Close</button>
      </div>

      {explanations.length === 0 ? (
        <p className="text-muted">No explanations available for this medication.</p>
      ) : (
        explanations.map((exp, i) => (
          <div key={i} className="explanation-card">
            <p className="explanation-card__diagnosis">
              Diagnosis: <span className="text-danger">{exp.diagnosis}</span>
            </p>
            <div className="causal-chain">
              {exp.causalChain?.split("→").map((step, j, arr) => (
                <span key={j} className="causal-chain__item">
                  <span className="causal-chain__step">{step.trim()}</span>
                  {j < arr.length - 1 && <span className="causal-chain__arrow">→</span>}
                </span>
              ))}
            </div>
          </div>
        ))
      )}
    </div>
  )
}