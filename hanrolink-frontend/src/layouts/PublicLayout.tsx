import Footer from '../components/Footer'
import Header from '../components/Header'
import { Outlet } from 'react-router-dom'

function PublicLayout() {
  return (
    <div className='flex min-h-dvh flex-col'>
      <Header isLoggedIn={false} />
      <main className='py-23 flex-1'>
        <Outlet />
      </main>
      <Footer />
    </div>
  )
}

export default PublicLayout
