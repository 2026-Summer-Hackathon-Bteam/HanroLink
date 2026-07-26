import logotitle from '../assets/HanroLink_logotitle.png'
import { Link } from 'react-router-dom'

function Header() {
  return (
    <>
      <header>
        <div>
          <h1>
            <img src={logotitle} alt="HanroLinkのロゴタイトル" />
          </h1>
          <div>
            <nav>
              <ul>
                <li>
                  <Link to="/signup">新規登録</Link>
                </li>
                <li>
                  <Link to="/login">ログイン</Link>
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
