import { useState, useEffect } from 'react'
import logotitle from '../assets/HanroLink_logotitle.png'
import { Link, useNavigate } from 'react-router-dom'
import { signOutUser } from '../features/auth/authService'
import {
  getPathAfterLogin,
  type CurrentAccount,
} from '../features/auth/authRouting'

type HeaderCommonProps = {
  showAfterScroll?: number
  showAfterElementId?: string
}

type HeaderProps = HeaderCommonProps &
  (
    | {
        // ログイン前はaccountは渡せない
        isLoggedIn: false
        account?: never
      }
    | {
        // ログイン後はaccountは必須で、roleとstatusはCurrentAccountで定義された組み合わせだけを許可
        isLoggedIn: true
        account: CurrentAccount
      }
  )

const menuItemsByName = {
  signup: { label: '新規登録', path: '/signup' },
  login: { label: 'ログイン', path: '/login' },
  products: { label: '商品を探す', path: '/products' },
  recruitments: { label: '募集情報を探す', path: '/procurement-requests' },
  adminMypage: { label: 'マイページ', path: '/mypage/admin' },
  supplierMyPage: { label: 'マイページ', path: '/mypage/supplier' },
  buyerMyPage: { label: 'マイページ', path: '/mypage/buyer' },
}

const navMenuByRoleAndStatus = {
  LOGGED_OUT: [menuItemsByName.signup, menuItemsByName.login],
  LOGGED_IN: {
    ADMIN: [
      menuItemsByName.adminMypage,
      menuItemsByName.products,
      menuItemsByName.recruitments,
    ],
    NOT_SUBMITTED: [],
    PENDING: {
      SUPPLIER: [menuItemsByName.supplierMyPage],
      BUYER: [menuItemsByName.buyerMyPage],
    },
    APPROVED: {
      SUPPLIER: [
        menuItemsByName.supplierMyPage,
        menuItemsByName.products,
        menuItemsByName.recruitments,
      ],
      BUYER: [
        menuItemsByName.buyerMyPage,
        menuItemsByName.products,
      ],
    },
  },
}

const getNavMenuList = (account: CurrentAccount | null) => {
  if (account === null) return navMenuByRoleAndStatus.LOGGED_OUT

  const { role, businessUserAccountRegistrationStatus: status } = account

  if (role === 'ADMIN') return navMenuByRoleAndStatus.LOGGED_IN.ADMIN
  if (role === null && status === 'NOT_SUBMITTED')
    return navMenuByRoleAndStatus.LOGGED_IN.NOT_SUBMITTED
  if (status === 'PENDING') {
    if (role === 'SUPPLIER') {
      return navMenuByRoleAndStatus.LOGGED_IN.PENDING.SUPPLIER
    }
    return navMenuByRoleAndStatus.LOGGED_IN.PENDING.BUYER
  }
  if (status === 'APPROVED') {
    if (role === 'SUPPLIER') {
      return navMenuByRoleAndStatus.LOGGED_IN.APPROVED.SUPPLIER
    }
    return navMenuByRoleAndStatus.LOGGED_IN.APPROVED.BUYER
  }
  return []
}

function Header(props: HeaderProps) {
  const { showAfterScroll = 0, showAfterElementId = '' } = props
  const isLoggedIn = props.isLoggedIn
  const account = props.isLoggedIn ? props.account : null

  const [isVisible, setIsVisible] = useState(
    showAfterElementId ? false : showAfterScroll === 0,
  )
  const [isMenuOpen, setIsMenuOpen] = useState(false)
  const [isSigningOut, setSigningOut] = useState(false)
  const [signOutError, setSignOutError] = useState('')

  const navigate = useNavigate()

  const navMenuList = getNavMenuList(account)
  const logoPath = account === null ? '/' : (getPathAfterLogin(account) ?? null)

  useEffect(() => {
    if (showAfterElementId) {
      const target = document.getElementById(showAfterElementId)

      if (target) {
        const observer = new IntersectionObserver(([entry]) => {
          setIsVisible(
            !entry.isIntersecting && entry.boundingClientRect.top < 0,
          )
        })

        observer.observe(target)

        return () => observer.disconnect()
      }
    }

    const handleScroll = () => {
      setIsVisible(window.scrollY >= showAfterScroll)
    }

    handleScroll()
    window.addEventListener('scroll', handleScroll, { passive: true })

    return () => {
      window.removeEventListener('scroll', handleScroll)
    }
  }, [showAfterScroll, showAfterElementId])

  const handleSignOut = async () => {
    if (isSigningOut) return

    setSignOutError('')
    setSigningOut(true)

    try {
      await signOutUser()

      navigate('/login', {
        replace: true,
      })
    } catch {
      setSignOutError('ログアウト処理に失敗しました。もう一度お試しください。')
    } finally {
      setSigningOut(false)
    }
  }

  return (
    <header
      className={`fixed left-0 right-0 top-0 z-50 px-4 pt-3 transition-transform duration-500 ease-out md:px-6 ${
        isVisible
          ? 'translate-y-0'
          : '-translate-y-[calc(100%+2px)] pointer-events-none'
      }`}
    >
      <div className="mx-auto flex justify-between items-center max-w-400 h-20 rounded-xl bg-bg/60 shadow-sm backdrop-blur-md">
        <h1 className="m-0 leading-none">
          {logoPath ? (
            <Link to={logoPath}>
              <img
                src={logotitle}
                alt="HanroLinkのロゴタイトル"
                className="h-16 md:h-20"
              />
            </Link>
          ) : (
            <img
              src={logotitle}
              alt="HanroLinkのロゴタイトル"
              className="h-16 md:h-20"
            />
          )}
        </h1>
        <div className="relative mr-4 md:mr-10">
          <nav
            id="header-navigation"
            className={`absolute right-0 top-full mt-2 w-42 origin-top-right rounded-xl bg-border shadow-md transition-[translate,scale,opacity] duration-300 ease-out
              ${
                isMenuOpen
                  ? 'translate-x-0 translate-y-0 scale-100 opacity-100 pointer-events-auto'
                  : 'translate-x-2 -translate-y-2 scale-90 opacity-0 pointer-events-none'
              }
              lg:static lg:mt-0 lg:h-12 lg:w-auto lg:translate-x-0 lg:translate-y-0
              lg:scale-100 lg:rounded-full lg:opacity-100 lg:pointer-events-auto`}
          >
            <ul className="flex flex-col rounded-xl overflow-hidden lg:rounded-full lg:h-full lg:flex-row">
              {navMenuList.map((menu, index) => (
                <li
                  key={menu.path}
                  className={
                    index > 0
                      ? 'relative lg:before:absolute lg:before:left-0 lg:before:top-1/2 lg:before:h-6 lg:before:-translate-y-1/2 lg:before:border-l lg:before:border-dotted lg:before:border-bg/60'
                      : undefined
                  }
                >
                  <Link
                    to={menu.path}
                    className="flex items-center h-12 textaccent text-bg px-7 underline-offset-2 hover:underline focus-visible:underline decoration-1"
                  >
                    {menu.label}
                  </Link>
                </li>
              ))}
              {isLoggedIn && (
                <li
                  className={
                    navMenuList.length > 0
                      ? 'relative lg:before:absolute lg:before:left-0 lg:before:top-1/2 lg:before:h-6 lg:before:-translate-y-1/2 lg:before:border-l lg:before:border-dotted lg:before:border-bg/60'
                      : undefined
                  }
                >
                  <button
                    type="button"
                    className="flex h-12 w-full items-center text-bg textaccent px-7 cursor-pointer disabled:cursor-not-allowed disabled:opacity-50 underline-offset-2 enabled:hover:underline focus-visible:underline decoration-1"
                    onClick={handleSignOut}
                    disabled={isSigningOut}
                  >
                    {isSigningOut ? 'ログアウト中' : 'ログアウト'}
                  </button>
                </li>
              )}
            </ul>
          </nav>
          {/* スマホ用ハンバーガーメニュー */}
          <button
            type="button"
            className="flex h-11 w-11 items-center justify-center rounded-lg text-2xl p-1 border border-border lg:hidden"
            aria-label={isMenuOpen ? 'メニューを閉じる' : 'メニューを開く'}
            aria-controls="header-navigation"
            aria-expanded={isMenuOpen}
            onClick={() => setIsMenuOpen((prev) => !prev)}
          >
            {/* ハンバーガメニューとバツ */}
            <span aria-hidden="true" className="relative block h-6 w-7">
              <span
                className={`absolute left-0 h-0.5 w-full bg-border transition-all duration-300 ${
                  isMenuOpen ? 'top-1/2 -translate-y-1/2 rotate-45' : 'top-1'
                }`}
              />

              <span
                className={`absolute left-0 top-1/2 h-0.5 w-full -translate-y-1/2 bg-border transition-all duration-300 ${
                  isMenuOpen ? 'scale-x-0 opacity-0' : 'scale-x-100 opacity-100'
                }`}
              />

              <span
                className={`absolute left-0 h-0.5 w-full bg-border transition-all duration-300 ${
                  isMenuOpen
                    ? 'bottom-1/2 translate-y-1/2 -rotate-45'
                    : 'bottom-1'
                }`}
              />
            </span>
          </button>
        </div>
      </div>
      {signOutError&& (
        <div
          role="alert"
          className="fixed left-1/2 top-8 z-60 flex w-[calc(100%-2rem)] max-w-md -translate-x-1/2 items-center justify-between gap-4 rounded-lg bg-bg/80 px-4 py-3 text-sm text-error shadow-lg"
        >
          <p>{signOutError}</p>
          <button
            type="button"
            aria-label="通知を閉じる"
            className="shrink-0 cursor-pointer text-xl leading-none"
            onClick={() => setSignOutError('')}
          >
            ×
          </button>
        </div>
      )}
    </header>
  )
}

export default Header
