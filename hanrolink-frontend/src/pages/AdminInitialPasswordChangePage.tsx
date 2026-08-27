import { useState, type SubmitEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  getAuthErrorMessage,
  confirmInitialPassword,
  signOutUser,
} from '../features/auth/authService'
import { getCurrentAccount } from '../features/auth/currentAccountService'

function AdminInitialPasswordChangePage() {
  const [password, setPassword] = useState('')
  const [passwordConfirm, setPasswordConfirm] = useState('')
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    setError('')

    if (password !== passwordConfirm) {
      setError('パスワードと確認用パスワードが一致していません。')
      return
    }
    setIsSubmitting(true)

    try {
      const result = await confirmInitialPassword({
        newPassword: password,
      })

      if (result.isSignedIn && result.nextStep.signInStep === 'DONE') {
        try {
          const account = await getCurrentAccount()

          if (account.role !== 'ADMIN') {
            throw new Error('管理者権限を確認できませんでした。')
          }

          navigate('/mypage/admin', {
            replace: true,
          })
        } catch (accountError: unknown) {
          const accountErrorMessage =
            accountError instanceof Error
              ? accountError.message
              : '自己情報の取得に失敗しました。'

          try {
            await signOutUser()
          } catch {
            // errorを更新せず、自己情報取得エラーを残す
          }
          setError(accountErrorMessage)
        }
        return
      }
      setError(
        `想定外のログイン状態です。管理者にお問い合わせください。（状態コード: ${result.nextStep.signInStep}）`,
      )
    } catch (e: unknown) {
      setError(getAuthErrorMessage(e))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
      <h2>管理者パスワード変更</h2>
      <p className="mb-8">
        管理者の初期パスワードは変更する必要があります。
        <br />
        新しいパスワードに変更してください。
      </p>

      <form
        onSubmit={handleSubmit}
        className="flex flex-col gap-8 max-w-120 mx-auto"
      >
        <div className="flex flex-col gap-1">
          <label htmlFor="password" className="text-xs self-start">
            新しいパスワード
          </label>
          <input
            type="password"
            name="password"
            id="password"
            required
            onChange={(e) => setPassword(e.target.value)}
            value={password}
            autoComplete="new-password"
          ></input>
        </div>
        <div className="flex flex-col gap-1">
          <label htmlFor="passwordConfirm" className="text-xs self-start">
            新しいパスワード（確認用）
          </label>
          <input
            type="password"
            name="passwordConfirm"
            id="passwordConfirm"
            required
            onChange={(e) => setPasswordConfirm(e.target.value)}
            value={passwordConfirm}
            autoComplete="new-password"
          ></input>
          {error && (
            <p role="alert" className="self-start text-error">
              {error}
            </p>
          )}
        </div>
        <button
          type="submit"
          className="h-9 w-45 mx-auto mt-8 rounded-full
            button-base button-form"
          disabled={isSubmitting}
        >
          {isSubmitting ? '送信中...' : '送信する'}
        </button>
      </form>
    </div>
  )
}

export default AdminInitialPasswordChangePage
