import { useState, useEffect, type SubmitEvent } from 'react'

type SignupFormData = {
  role: 'supplier' | 'buyer' | null
  email: string
  password: string
  passwordConfirm: string
}

type FormErrors = {
  role?: string
  email?: string
  password?: string
  passwordConfirm?: string
  form?: string
}

function SignupPage() {
  const [formData, setFormData] = useState<SignupFormData>({
    role: null,
    email: '',
    password: '',
    passwordConfirm: '',
  })
  const [errors, setErrors] = useState<FormErrors>({})

  const handleSubmit = (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    // Cognitoの送信処理
  }

  useEffect(() => {
    const timeoutId = window.setTimeout(() => {
      setErrors((prev) => ({
        ...prev,
        passwordConfirm: !formData.passwordConfirm
          ? undefined
          : formData.password !== formData.passwordConfirm
            ? 'パスワードが一致しません'
            : undefined,
      }))
    }, 400)

    return () => window.clearTimeout(timeoutId)
  }, [formData.password, formData.passwordConfirm])

  return (
    <>
      <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
        <h2>新規登録</h2>
        <p className="mb-8">
          このサイトは事業者専用です。一般の方の登録はご遠慮ください。
          <br />
          １事業者につきサプライヤー、バイヤーの両方に登録することはできません。
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
          <fieldset className="flex flex-col gap-1">
            <legend className="text-xs text-left">
              サプライヤー／バイヤー
            </legend>
            <div className="flex gap-4 justify-start pl-4">
              <div className="flex items-center gap-1">
                <input
                  type="radio"
                  name="role"
                  id="supplier"
                  value="supplier"
                  required
                  checked={formData.role === 'supplier'}
                  onChange={() =>
                    setFormData((prev) => ({
                      ...prev,
                      role: 'supplier',
                    }))
                  }
                />
                <label htmlFor="supplier" className="text-base">
                  サプライヤー
                </label>
              </div>
              <div className="flex items-center gap-1">
                <input
                  type="radio"
                  name="role"
                  id="buyer"
                  value="buyer"
                  checked={formData.role === 'buyer'}
                  onChange={() =>
                    setFormData((prev) => ({ ...prev, role: 'buyer' }))
                  }
                />
                <label htmlFor="buyer" className="text-base">
                  バイヤー
                </label>
              </div>
            </div>
          </fieldset>
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
          >
            新規登録
          </button>
        </form>
      </div>
    </>
  )
}

export default SignupPage
