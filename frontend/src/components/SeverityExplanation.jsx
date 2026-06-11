export default function SeverityExplanation({ factor, medicationName, explanations, onClose }) {
  return (
    <div className="explanation-overlay">
      <div className="explanation-overlay__header">
        <div>
          <p className="eyebrow">Interaction explanation</p>
          <p className="explanation-overlay__title">
            <strong>{medicationName}</strong> + <strong>{factor}</strong>
          </p>
        </div>
        <button className="btn btn--ghost" onClick={onClose}>✕</button>
      </div>

      {explanations.length === 0 && (
        <p className="text-muted">No causal chain found for this interaction.</p>
      )}

      {explanations.map((e, i) => (
        <div key={i} className="explanation-card">
          <p className="explanation-card__diagnosis" style={{ marginBottom: 8 }}>
            Risk level: <span className={`badge badge--${(e.finalRiskLevel ?? "").toLowerCase()}`}>
              {e.finalRiskLevel ?? "UNKNOWN"}
            </span>
          </p>
          <div className="causal-chain">
            {e.causalChain.split(" -> ").map((step, j, arr) => (
              <span key={j} className="causal-chain__item">
                <span className="causal-chain__step">{step}</span>
                {j < arr.length - 1 && <span className="causal-chain__arrow">→</span>}
              </span>
            ))}
          </div>
        </div>
      ))}
    </div>
  )
}