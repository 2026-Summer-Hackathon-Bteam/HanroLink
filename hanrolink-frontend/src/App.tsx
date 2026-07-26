import { Routes, Route } from 'react-router-dom'
import './App.css'
import GuestPage from './pages/GuestPage'
import SignupPage from './pages/SignupPage'
import SignupCompletePage from './pages/SignupCompletePage'
import LoginPage from './pages/LoginPage'
import SignupConfirmPage from './pages/SignupConfirmPage'

function App() {
  return (
    <>
      <main>
        <Routes>
          <Route path="/" element={<GuestPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/signup/confirm" element={<SignupConfirmPage />} />
          <Route path="/signup/complete" element={<SignupCompletePage />} />
          <Route path="/login" element={<LoginPage />} />
        </Routes>
      </main>
    </>
  )
}

export default App
