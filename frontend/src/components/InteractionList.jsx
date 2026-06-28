import { INTERACTION_TYPE_LABEL } from "../constants"
import SeverityBadge from "./SeverityBadge"

export default function InteractionList({ interactions, onExplain }) {
  return (
    <div className="interaction-list">
      {interactions.map((item, i) => {
        const typeLabel = INTERACTION_TYPE_LABEL[item.interactionType] ?? item.interactionType
        const medLabel = item.existingMedication
          ? `${item.existingMedication.name} + ${item.newMedication.name}`
          : item.newMedication.name

        return (
          <div key={i} className="interaction-item">
            <div className="interaction-item__header">
              <div className="interaction-item__badges">
                <SeverityBadge level={item.severity} />
                <span className="tag">{typeLabel}</span>
              </div>
              <span className="interaction-item__score">Score: {item.score}</span>
            </div>
            <p className="interaction-item__meds">{medLabel}</p>
            <p className="interaction-item__reason">{item.reason}</p>
            {item.factor && (
              <button
                className="btn btn--ghost"
                style={{ marginTop: 8, fontSize: 12, padding: "4px 10px" }}
                onClick={() => onExplain(item.factor, item.newMedication.name, item.severity)}
              >
                Why?
              </button>
            )}
          </div>
        )
      })}
    </div>
  )
}