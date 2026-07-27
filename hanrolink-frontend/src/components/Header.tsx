import logotitle from '../assets/HanroLink_logotitle.png'
import { Link } from 'react-router-dom'

function Header() {
  return (
    <>
      <header className="fixed left-0 right-0 top-0 z-50 px-4 pt-3 md:px-6">
        <div className="mx-auto flex justify-between items-center max-w-400 h-20 rounded-xl bg-bg/60 shadow-sm backdrop-blur-md">
          <h1 className="m-0 leading-none">
            <img
              src={logotitle}
              alt="HanroLinkのロゴタイトル"
              className="h-20"
            />
          </h1>
          <div className="mr-10">
            <nav className="rounded-full h-12">
              <ul className="rounded-full flex h-full items-center overflow-hidden">
                <li>
                  <Link
                    to="/signup"
                    className="block textaccent text-bg px-7 py-4 bg-border transition hover:bg-border/80"
                  >
                    新規登録
                  </Link>
                </li>
                <li className="relative before:absolute before:left-0 before:top-1/2 before:h-6 before:-translate-y-1/2 before:border-l before:border-dotted before:border-bg/60">
                  <Link
                    to="/login"
                    className="block textaccent text-bg px-7 py-4 bg-border transition hover:bg-border/80"
                  >
                    ログイン
                  </Link>
                </li>
              </ul>
            </nav>
            <p>{/* ログアウトボタンがあるときはここに入れる */}</p>
          </div>
        </div>
      </header>
    </>
  )
}

export default Header
