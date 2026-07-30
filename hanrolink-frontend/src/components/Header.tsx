import { useState, useEffect } from 'react'
import logotitle from '../assets/HanroLink_logotitle.png'
import { Link } from 'react-router-dom'

type HeaderProps = {
  showAfterScroll?: number
  showAfterElementId?: string
}

function Header({ showAfterScroll = 0, showAfterElementId = '' }: HeaderProps) {
  const [isVisible, setIsVisible] = useState(
    showAfterElementId ? false : showAfterScroll === 0,
  )
  const [isMenuOpen, setIsMenuOpen] = useState(false)

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
          <img
            src={logotitle}
            alt="HanroLinkのロゴタイトル"
            className="h-16 md:h-20"
          />
        </h1>
        <div className="relative mr-4 md:mr-10">
          <nav
            id="guest-navigation"
            className={`absolute right-0 top-full mt-2 w-40 origin-top-right rounded-xl bg-border shadow-md transition-[translate,scale,opacity] duration-300 ease-out
              ${
                isMenuOpen
                  ? 'translate-x-0 translate-y-0 scale-100 opacity-100 pointer-events-auto'
                  : 'translate-x-2 -translate-y-2 scale-90 opacity-0 pointer-events-none'
              }
              md:static md:mt-0 md:h-12 md:w-auto md:translate-x-0 md:translate-y-0
              md:scale-100 md:rounded-full md:opacity-100 md:pointer-events-auto`}
          >
            <ul className="flex flex-col rounded-xl overflow-hidden md:rounded-full md:h-full md:flex-row">
              <li>
                <Link
                  to="/signup"
                  className="flex items-center h-12 textaccent text-bg px-7 bg-border transition hover:bg-border/80"
                >
                  新規登録
                </Link>
              </li>
              <li className="relative md:before:absolute md:before:left-0 md:before:top-1/2 md:before:h-6 md:before:-translate-y-1/2 md:before:border-l md:before:border-dotted md:before:border-bg/60">
                <Link
                  to="/login"
                  className="flex items-center h-12 textaccent text-bg px-7 bg-border transition hover:bg-border/80"
                >
                  ログイン
                </Link>
              </li>
            </ul>
          </nav>
          {/* スマホ用ハンバーガーメニュー */}
          <button
            type="button"
            className="flex h-11 w-11 items-center justify-center rounded-lg text-2xl p-1 border border-border md:hidden"
            aria-label={isMenuOpen ? 'メニューを閉じる' : 'メニューを開く'}
            aria-controls="guest-navigation"
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
          <p>{/* ログアウトボタンがあるときはここに入れる */}</p>
        </div>
      </div>
    </header>
  )
}

export default Header
