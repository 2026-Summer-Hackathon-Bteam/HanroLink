import { useState, useEffect } from 'react'
import { Outlet } from 'react-router-dom'
import Footer from '../components/Footer'
import type { CurrentAccount } from '../features/auth/authRouting'
import Header from '../components/Header'
import { getCurrentAccount } from '../features/auth/currentAccountService'

function AuthenticatedLayout() {
  const [account, setAccount] =
    useState<CurrentAccount | null>(null)
  const [error, setError] = useState('')

  useEffect(() => {
    let isCancelled = false

    const loadCurrentAccount = async () => {
      try {
        const result = await getCurrentAccount()

        if (!isCancelled) {
          setAccount(result)
        }
      } catch (error: unknown) {
        if (!isCancelled) {
          setError(
            error instanceof Error
              ? error.message
              : '自己情報の取得に失敗しました。',
          )
        }
      }
    }

    void loadCurrentAccount()

    return () => {
      isCancelled = true
    }
  }, [])

  if(error) {
    return <p role='alert' className='text-error text-center py-10'>{error}</p>
  }

  if(!account){
    return <p className='text-center py-10'>読み込み中...</p>
  }

  return (
    <div className="flex min-h-dvh flex-col">
      <Header isLoggedIn={true} account={account} />
      <main className="flex flex-1 flex-col">
        <Outlet />
      </main>
      <Footer />
    </div>
  )
}

export default AuthenticatedLayout
