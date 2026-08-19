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
import ProductCreatePage from './pages/ProductCreatePage'
import BuyerProfilePage from './pages/BuyerProfilePage'
import ProcurementRequestCreatePage from './pages/ProcurementRequestCreatePage'
import ProductDetailPage from './pages/ProductDetailPage'
import ProductEditPage from './pages/ProductEditPage'
import ProcurementRequestDetailPage from './pages/ProcurementRequestDetailPage'
import ProcurementRequestEditPage from './pages/ProcurementRequestEditPage'
import ProductSearchPage from './pages/ProductSearchPage'
import ProcurementRequestSearchPage from './pages/ProcurementRequestSearchPage'
import ChatPage from './pages/ChatPage'
import AdminInitialPasswordChangePage from './pages/AdminInitialPasswordChangePage'

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
          <Route
            path="/login/new-password"
            element={<AdminInitialPasswordChangePage />}
          />
        </Route>

        <Route element={<AuthenticatedLayout />}>
          <Route element={<AuthenticatedPageLayout />}>
            <Route
              path="onboarding/business"
              element={<BusinessProfileSetupPage />}
            />
            <Route path="signup/complete" element={<SignupCompletePage />} />
            <Route
              path="admin/approvals/:businessId"
              element={<AdminBusinessApprovalDetailPage />}
            />
            <Route
              path="buyer/:businessId"
              element={<BuyerProfilePage />}
            />
            <Route path="/products" element={<ProductSearchPage />} />
            <Route path="products/new" element={<ProductCreatePage />} />
            <Route
              path="/products/:productId"
              element={<ProductDetailPage />}
            />
            <Route
              path="/products/:productId/edit"
              element={<ProductEditPage />}
            />
            <Route
              path="/procurement-requests"
              element={<ProcurementRequestSearchPage />}
            />
            <Route
              path="/procurement-requests/new"
              element={<ProcurementRequestCreatePage />}
            />
            <Route
              path="/procurement-requests/:procurementRequestId"
              element={<ProcurementRequestDetailPage />}
            />
            <Route
              path="/procurement-requests/:procurementRequestId/edit"
              element={<ProcurementRequestEditPage />}
            />
          </Route>

          <Route element={<MyPageRayout />}>
            <Route path="mypage/admin" element={<AdminMyPage />} />
            <Route path="mypage/supplier" element={<SupplierMyPage />} />
            <Route path="mypage/buyer" element={<BuyerMyPage />} />
            <Route path="/chats/:channelId" element={<ChatPage />} />
          </Route>
        </Route>
      </Routes>
    </>
  )
}

export default App
