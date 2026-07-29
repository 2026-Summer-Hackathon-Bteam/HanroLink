import { Routes, Route } from 'react-router-dom'
import GuestPage from './pages/GuestPage'
import SignupPage from './pages/SignupPage'
import SignupCompletePage from './pages/SignupCompletePage'
import LoginPage from './pages/LoginPage'
import SignupConfirmPage from './pages/SignupConfirmPage'
import PublicLayout from './layouts/PublicLayout'
import BusinessProfileSetupPage from './pages/BusinessProfileSetupPage'

function App() {
  return (
    <>
      <Routes>
        <Route element={<PublicLayout />}>
          <Route index element={<GuestPage />} />
          <Route path="signup" element={<SignupPage />} />
          <Route path="signup/confirm" element={<SignupConfirmPage />} />
          <Route path="signup/complete" element={<SignupCompletePage />} />
          <Route path="login" element={<LoginPage />} />
          <Route path="onboarding/business" element={<BusinessProfileSetupPage />} />
        </Route>
      </Routes>
    </>
  )
}

export default App
