import { Routes, Route } from 'react-router-dom'
import GuestPage from './pages/GuestPage'
import SignupPage from './pages/SignupPage'
import SignupCompletePage from './pages/SignupCompletePage'
import LoginPage from './pages/LoginPage'
import SignupConfirmPage from './pages/SignupConfirmPage'
import PublicLayout from './layouts/PublicLayout'
import BusinessProfileSetupPage from './pages/BusinessProfileSetupPage'
import GuestLayout from './layouts/GuestLayout'
import AdminMyPage from './pages/AdminMyPage'
import SupplierMyPage from './pages/SupplierMyPage'
import BuyerMyPage from './pages/BuyerMyPage'
import AuthenticatedLayout from './layouts/AuthenticatedLayout'
import AuthenticatedPageLayout from './layouts/AuthenticatedPageLayout'
import MyPageRayout from './layouts/MyPageLayout'
import AdminBusinessApprovalDetailPage from './pages/AdminBusinessApprovalDetailPage'

function App() {
  return (
    <>
      <Routes>
        <Route element={<GuestLayout />}>
          <Route index element={<GuestPage />} />
        </Route>

        <Route element={<PublicLayout />}>
          <Route path="signup" element={<SignupPage />} />
          <Route path="signup/confirm" element={<SignupConfirmPage />} />

          <Route path="login" element={<LoginPage />} />
        </Route>

        <Route element={<AuthenticatedLayout />}>
          <Route element={<AuthenticatedPageLayout />}>
            <Route
              path="onboarding/business"
              element={<BusinessProfileSetupPage />}
            />
            <Route path="signup/complete" element={<SignupCompletePage />} />
            <Route path="admin/approvals/:businessUserAccountId" element={<AdminBusinessApprovalDetailPage />} />
          </Route>

          <Route element={<MyPageRayout />}>
            <Route path="mypage/admin" element={<AdminMyPage />} />
            <Route path="mypage/supplier" element={<SupplierMyPage />} />
            <Route path="mypage/buyer" element={<BuyerMyPage />} />
          </Route>
        </Route>
      </Routes>
    </>
  )
}

export default App
