import { SEVERITY_META } from "../constants"

export default function SeverityBadge({ level }) {
  const meta = SEVERITY_META[level] ?? SEVERITY_META.SAFE
  return <span className={meta.className}>{meta.label}</span>
}