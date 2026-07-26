import { useState, type SubmitEvent } from 'react'

type SelectedRole = 'supplier' | 'buyer' | null

function SignupPage() {
  const [selectedRole, setSelectedRole] = useState<SelectedRole>(null)
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [passwordConfirm, setPasswordConfirm] = useState('')

  const handleSubmit = (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    // Cognitoの送信処理
  }

  return (
    <>
      <div>
        <h2>新規登録</h2>
        <p>
          このサイトは事業者専用です。一般の方の登録はご遠慮ください。
          <br />
          １事業者につきサプライヤー、バイヤーの両方に登録することはできません。
          <br />
          登録後に管理者にて審査を行いますので、サービスの使用は審査完了後となります。
        </p>
        <section>
          <h3>新規登録の流れ</h3>
          <p>
            新規登録（この画面）でアカウント情報を入力し、送信してください。
          </p>
          <p>&darr;</p>
          <p>
            登録したメールアドレスに確認コードが送信されますので、コード確認の画面でコードを入力してください。
          </p>
          <p>&darr;</p>
          <p>
            会社情報等登録画面で会社情報（会社名、所在地、担当者名など）を入力してください。
          </p>
          <p>&darr;</p>
          <p>
            会社情報送信後、管理者が審査を行います（3日程度かかります）。審査完了後にアプリを使用することができます。
          </p>
        </section>
      </div>
      <form onSubmit={handleSubmit}>
        <fieldset>
          <legend>サプライヤー／バイヤー</legend>
          <div>
            <input
              type="radio"
              name="role"
              id="supplier"
              value="supplier"
              required
              checked={selectedRole === 'supplier'}
              onChange={() => setSelectedRole('supplier')}
            />
            <label htmlFor="supplier">サプライヤー</label>
            <input
              type="radio"
              name="role"
              id="buyer"
              value="buyer"
              checked={selectedRole === 'buyer'}
              onChange={() => setSelectedRole('buyer')}
            />
            <label htmlFor="buyer">バイヤー</label>
          </div>
        </fieldset>
        <div>
          <label htmlFor="email">メールアドレス</label>
          <input
            type="email"
            name="email"
            id="email"
            required
            onChange={(e) => setEmail(e.target.value)}
            value={email}
          ></input>
        </div>
        <div>
          <label htmlFor="password">パスワード</label>
          <input
            type="password"
            name="password"
            id="password"
            required
            onChange={(e) => setPassword(e.target.value)}
            value={password}
          ></input>
        </div>
        <div>
          <label htmlFor="passwordConfirm">パスワード（確認用）</label>
          <input
            type="password"
            name="passwordConfirm"
            id="passwordConfirm"
            required
            onChange={(e) => setPasswordConfirm(e.target.value)}
            value={passwordConfirm}
          ></input>
        </div>
        <button type="submit">新規登録</button>
      </form>
    </>
  )
}

export default SignupPage
