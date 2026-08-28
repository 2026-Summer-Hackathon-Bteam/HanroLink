import { Outlet, useOutletContext } from 'react-router-dom'
import type { CurrentAccount } from '../features/auth/authRouting'

function AuthenticatedPageLayout() {
  const context = useOutletContext<{ account: CurrentAccount }>()
  return (
    <div className="py-23">
      <Outlet context={context} />
    </div>
  )
}

export default AuthenticatedPageLayout
