import { useState, type SubmitEvent } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { getAuthErrorMessage, signInUser } from '../features/auth/authService'

type LocationState = {
  email?: string
  message?: string
}

function LoginPage() {
  const location = useLocation()
  const state = location.state as LocationState | null
  const [email, setEmail] = useState(state?.email ?? '')
  const message = state?.message ?? ''
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    setError('')
    setIsSubmitting(true)

    // cognitoの送信処理
    try {
      const result = await signInUser({
        email,
        password,
      })

      if (result.isSignedIn && result.nextStep.signInStep === 'DONE') {
        // Cognitoのアクセストークン取得
        // バックエンドにトークンを送付して自己情報を取得する
        // レスポンスのステータスにより、遷移先を分岐する
        return
      }

      if (result.nextStep.signInStep === 'CONFIRM_SIGN_UP') {
        navigate('/signup/confirm', {
          state: {
            email: email.trim(),
          },
        })
        return
      }
      // 上の２つ以外の状態をまとめて処理
      setError(
        `想定外のログイン状態です。管理者にお問い合わせください。（状態コード: ${result.nextStep.signInStep}）`,
      )
    } catch (error: unknown) {
      setError(getAuthErrorMessage(error))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <>
      <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
        <h2>ログイン</h2>
        {message && <p className="mb-8">{message}</p>}
        <form
          onSubmit={handleSubmit}
          className="flex flex-col gap-8 max-w-120 mx-auto"
        >
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
          <div className="flex flex-col gap-1">
            <label htmlFor="password" className="text-xs self-start">
              パスワード
            </label>
            <input
              type="password"
              name="password"
              id="password"
              required
              onChange={(e) => setPassword(e.target.value)}
              value={password}
              autoComplete="current-password"
            ></input>
          </div>
          <button
            type="submit"
            className="h-9 w-45 mx-auto mt-8 rounded-full border border-accent bg-accentbg"
            disabled={isSubmitting}
          >
            {isSubmitting ? '送信中...' : 'ログイン'}
          </button>
          {error && (
            <p role="alert" className="text-error">
              {error}
            </p>
          )}
        </form>
      </div>
    </>
  )
}

export default LoginPage
