import SeverityBadge from "./SeverityBadge"

export default function SafetyReport({ report }) {
  const severityClass = (report.highestSeverity ?? "SAFE").toLowerCase()

  return (
    <div className="panel">
      <div className="safety-report__header">
        <p className="section-label">Safety Report</p>
        <SeverityBadge level={report.highestSeverity ?? "SAFE"} />
      </div>
      <p className="safety-report__summary">{report.summary}</p>
      {report.recommendation && (
        <div className={`safety-report__recommendation safety-report__recommendation--${severityClass}`}>
          {report.recommendation}
        </div>
      )}
    </div>
  )
}