import Footer from '../components/Footer'
import Header from '../components/Header'
import { Outlet } from 'react-router-dom'

function PublicLayout() {
  return (
    <>
      <Header />
      <main>
        <Outlet />
      </main>
      <Footer />
    </>
  )
}

export default PublicLayout
