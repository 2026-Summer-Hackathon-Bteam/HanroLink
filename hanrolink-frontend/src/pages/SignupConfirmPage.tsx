import { useState, type SubmitEvent } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'

type LocationState = {
  email?: string
}

function SignupConfirmPage() {
  const location = useLocation()
  const state = location.state as LocationState | null
  const [email, setEmail] = useState(state?.email ?? '')
  const [confirmCode, setConfirmCode] = useState('')
  const navigate = useNavigate()

  const handleSubmit = (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()

    // Cognitoへの送信処理

    // 成功後にログイン画面へ遷移
    navigate('/login', {
      replace: true,
      state: {
        email,
        message:
          'メールアドレスの確認が完了しました。ログインして会社情報を入力してください。',
      },
    })
  }

  return (
    <>
      <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
        <h2>コード確認</h2>
        <p className="mb-4">
          ご登録のメールアドレスに送信された確認コードを入力し、
          <br />
          「確認する」ボタンを押してください。
        </p>
        <p className="mb-8">
          新規登録後にブラウザを閉じてしまった等によりメールアドレス入力欄が表示されている場合は、
          <br />
          お手数ですが、メールアドレスも併せて入力してください。
        </p>
        <form
          onSubmit={handleSubmit}
          className="flex flex-col gap-8 max-w-120 mx-auto"
        >
          {state?.email ? (
            <p>確認コード送信先：{email}</p>
          ) : (
            <div className="flex flex-col gap-1">
              <label htmlFor="email" className="text-xs self-start">
                メールアドレス
              </label>
              <input
                type="email"
                name="email"
                id="email"
                required
                onChange={(e) => setEmail(e.target.value)}
                value={email}
              ></input>
            </div>
          )}
          <div className="flex flex-col gap-1">
            <label htmlFor="confirmCode" className="text-xs self-start">
              確認コード
            </label>
            <input
              type="text"
              name="confirmCode"
              id="confirmCode"
              required
              onChange={(e) => setConfirmCode(e.target.value)}
              value={confirmCode}
              inputMode="numeric"
              autoComplete="one-time-code"
            ></input>
          </div>
          <button
            type="submit"
            className="h-9 w-45 mx-auto mt-8 rounded-full border border-accent bg-accentbg"
          >
            確認する
          </button>
        </form>
      </div>
    </>
  )
}

export default SignupConfirmPage
