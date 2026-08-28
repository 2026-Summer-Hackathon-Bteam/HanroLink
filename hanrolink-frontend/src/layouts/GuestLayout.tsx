import Footer from '../components/Footer'
import Header from '../components/Header'
import { Outlet } from 'react-router-dom'

function GuestLayout() {
  return (
    <div className="flex min-h-dvh flex-col">
      <Header isLoggedIn={false} showAfterElementId="guest-header-trigger" />
      <main className="pb-23 flex-1">
        <Outlet />
      </main>
      <Footer />
    </div>
  )
}

export default GuestLayout
