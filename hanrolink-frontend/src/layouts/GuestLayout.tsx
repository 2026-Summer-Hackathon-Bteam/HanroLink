import Footer from '../components/Footer'
import Header from '../components/Header'
import { Outlet } from 'react-router-dom'

function GuestLayout() {
  return (
    <>
      <Header showAfterScroll={400}/>
      <main className='pb-23'>
        <Outlet />
      </main>
      <Footer />
    </>
  )
}

export default GuestLayout