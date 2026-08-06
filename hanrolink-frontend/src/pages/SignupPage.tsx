import { useState, useEffect, type SubmitEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAuthErrorMessage, signUpUser } from '../features/auth/authService'

type SignupFormData = {
  email: string
  password: string
  passwordConfirm: string
}

type FormErrors = {
  email?: string
  password?: string
  passwordConfirm?: string
  form?: string
}

const getPasswordConfirmError = (
  password: string,
  passwordConfirm: string,
): string | undefined => {
  return password === passwordConfirm ? undefined : 'パスワードが一致しません'
}

function SignupPage() {
  const [formData, setFormData] = useState<SignupFormData>({
    email: '',
    password: '',
    passwordConfirm: '',
  })
  const [errors, setErrors] = useState<FormErrors>({})
  const navigate = useNavigate()
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    const timeoutId = window.setTimeout(() => {
      setErrors((prev) => ({
        ...prev,
        passwordConfirm: !formData.passwordConfirm
          ? undefined
          : getPasswordConfirmError(
              formData.password,
              formData.passwordConfirm,
            ),
      }))
    }, 400)

    return () => window.clearTimeout(timeoutId)
  }, [formData.password, formData.passwordConfirm])

  const handleSubmit = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    // 送信前にエラーがないかを確認
    const nextErrors: FormErrors = {}
    const passwordConfirmError = getPasswordConfirmError(
      formData.password,
      formData.passwordConfirm,
    )
    if (passwordConfirmError) {
      nextErrors.passwordConfirm = passwordConfirmError
    }
    //他のバリデーション処理

    setErrors(nextErrors)

    if (Object.keys(nextErrors).length > 0) return

    // Cognitoの送信処理

    setIsSubmitting(true)

    try {
      const result = await signUpUser({
        email: formData.email,
        password: formData.password,
      })

      if (result.nextStep.signUpStep === 'CONFIRM_SIGN_UP') {
        navigate('/signup/confirm', {
          state: {
            email: formData.email.trim(),
          },
        })
        return
      }

      setErrors((prev) => ({
        ...prev,
        form: '新規登録を続行できませんでした。',
      }))
    } catch (error: unknown) {
      setErrors((prev) => ({
        ...prev,
        form: getAuthErrorMessage(error),
      }))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <>
      <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
        <h2>新規登録</h2>
        <p className="mb-8">
          このサイトは事業者専用です。一般の方の登録はご遠慮ください。
          <br />
          登録後に管理者にて審査を行いますので、サービスの使用は審査完了後となります。
        </p>
        <section className="mb-8 mx-auto border-4 border-textbg p-4 w-fit">
          <h3 className="text-2xl mb-4">新規登録の流れ</h3>
          <p>
            新規登録（この画面）でアカウント情報を入力し、送信してください。
          </p>
          <p>&darr;</p>
          <p>
            登録したメールアドレスに確認コードが送信されますので、コード確認の画面でコードを入力してください。
          </p>
          <p>&darr;</p>
          <p>ログイン画面でログインしてください。</p>
          <p>&darr;</p>
          <p>
            会社情報等登録画面で会社情報（会社名、所在地、担当者名など）を入力してください。
          </p>
          <p>&darr;</p>
          <p>
            会社情報送信後、管理者が審査を行います（3日程度かかります）。審査完了後にアプリを使用することができます。
          </p>
        </section>

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
              onChange={(e) =>
                setFormData((prev) => ({ ...prev, email: e.target.value }))
              }
              value={formData.email}
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
              onChange={(e) =>
                setFormData((prev) => ({ ...prev, password: e.target.value }))
              }
              value={formData.password}
            ></input>
          </div>
          <div className="flex flex-col gap-1">
            <label htmlFor="passwordConfirm" className="text-xs self-start">
              パスワード（確認用）
            </label>
            <input
              type="password"
              name="passwordConfirm"
              id="passwordConfirm"
              required
              onChange={(e) =>
                setFormData((prev) => ({
                  ...prev,
                  passwordConfirm: e.target.value,
                }))
              }
              value={formData.passwordConfirm}
            ></input>
            {errors.passwordConfirm && (
              <p className="self-start text-error">{errors.passwordConfirm}</p>
            )}
          </div>
          <button
            type="submit"
            className="h-9 w-45 mx-auto mt-8 rounded-full border border-accent bg-accentbg"
            disabled={isSubmitting}
          >
            {isSubmitting ? '送信中...' : '新規登録'}
          </button>
          {errors.form && (
            <p role="alert" className="text-error">
              {errors.form}
            </p>
          )}
        </form>
      </div>
    </>
  )
}

export default SignupPage
