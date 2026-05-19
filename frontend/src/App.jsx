import { useState } from 'react'
import './App.css'

function App() {
  const [started, setStarted] = useState(false)

  return (
    <div className="app-shell">
      {!started ? (
        <div className="home-page">
          <span className="eyebrow">Clinical intelligence</span>
          <h1>Drug interaction safety starts here</h1>
          <p>
            Build a safer patient workflow with a simple medical dashboard experience. Click Get Started to continue.
          </p>
          <button className="primary-button" onClick={() => setStarted(true)}>
            Get Started
          </button>
        </div>
      ) : (
        <div className="feature-grid">
          <div className="feature-card">
            <div className="feature-badge">Patient Management</div>
            <h2>Manage patient data</h2>
            <p>Track medications, diagnoses, allergies and clinical notes in one polished view.</p>
            <button className="secondary-button">Open patient management</button>
          </div>
          <div className="feature-card">
            <div className="feature-badge">Drug Interaction Checker</div>
            <h2>Check drug safety</h2>
            <p>Analyze therapy combinations and spot interactions before you prescribe.</p>
            <button className="secondary-button">Open interaction checker</button>
          </div>
        </div>
      )}
    </div>
  )
}

export default App
