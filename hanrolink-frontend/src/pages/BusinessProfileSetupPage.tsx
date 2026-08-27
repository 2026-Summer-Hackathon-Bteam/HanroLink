import { useState, type SubmitEvent, type ChangeEvent, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import FormRow from '../components/FormRow'
import {
  getOnboardingInitialData,
  submitOnboarding,
} from '../features/onboarding/onboardingService'

type BusinessProfileFormData = {
  role: 'SUPPLIER' | 'BUYER' | null
  businessName: string
  businessNameKana: string
  businessPostalCode: string
  businessAddressPrefecture: string
  businessAddressMunicipalityStreet: string
  businessAddressBuilding: string
  businessPhoneNumber: string
  businessWebsiteUrl: string
  contactLastName: string
  contactFirstName: string
  contactLastNameKana: string
  contactFirstNameKana: string
  contactPhoneNumber: string
}

function BusinessProfileSetupPage() {
  const [formData, setFormData] = useState<BusinessProfileFormData>({
    role: null,
    businessName: '',
    businessNameKana: '',
    businessPostalCode: '',
    businessAddressPrefecture: '',
    businessAddressMunicipalityStreet: '',
    businessAddressBuilding: '',
    businessPhoneNumber: '',
    businessWebsiteUrl: '',
    contactLastName: '',
    contactFirstName: '',
    contactLastNameKana: '',
    contactFirstNameKana: '',
    contactPhoneNumber: '',
  })
  const [email, setEmail] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    let isCancelled = false

    const loadOnboardingInitialData = async () => {
      try {
        const result = await getOnboardingInitialData()

        if (!isCancelled) {
          return setEmail(result.email)
        }
      } catch (error: unknown) {
        if (!isCancelled) {
          setError(
            error instanceof Error
              ? error.message
              : '初期データの取得に失敗しました。',
          )
        }
      }
    }
    void loadOnboardingInitialData()

    return () => {
      isCancelled = true
    }
  }, [])

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const handleSubmit = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    if (isSubmitting) return

    if (!formData.role) return

    setIsSubmitting(true)
    setError('')

    const submitData = {
      business: {
        role: formData.role,
        name: formData.businessName.trim(),
        nameKana: formData.businessNameKana.trim(),
        websiteUrl: formData.businessWebsiteUrl.trim() || undefined,
        addressPostalCode: formData.businessPostalCode.trim(),
        addressPrefecture: formData.businessAddressPrefecture.trim(),
        addressMunicipalityStreet:
          formData.businessAddressMunicipalityStreet.trim(),
        addressBuilding: formData.businessAddressBuilding.trim() || undefined,
        phoneNumber: formData.businessPhoneNumber.trim(),
      },
      businessUserAccount: {
        lastName: formData.contactLastName.trim(),
        firstName: formData.contactFirstName.trim(),
        lastNameKana: formData.contactLastNameKana.trim(),
        firstNameKana: formData.contactFirstNameKana.trim(),
        phoneNumber: formData.contactPhoneNumber.trim(),
      },
    }

    try {
      const result = await submitOnboarding(submitData)

      if (result.businessUserAccountRegistrationStatus !== 'PENDING') {
        throw new Error('予期しない登録ステータスが返されました。')
      }

      navigate('/signup/complete', {
        replace: true,
        state: { role: formData.role },
      })
    } catch (error: unknown) {
      setError(
        error instanceof Error
          ? error.message
          : '事業者情報の登録に失敗しました。',
      )
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <>
      <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
        <h2>事業者情報登録</h2>
        <p className="mb-8">
          ご登録ありがとうございます。
          <br />
          サービスをご利用いただくため、お手数ですが以下の情報をご入力のうえ送信してください。
          <br />
          各事業者様に安心してご利用いただくため、ご入力いただいた情報をもとに審査を行います。
          <br />
          審査には通常3日程度お時間をいただきますので、あらかじめご了承ください。
          <br />
          なお、１事業者につきサプライヤー、バイヤーの両方に登録することはできません。          
        </p>
        <form onSubmit={handleSubmit} className="flex flex-col mx-auto">
          <div className="overflow-hidden border border-border divide-y divide-border">
            <fieldset className="m-0 min-w-0 border-0 p-0">
              <legend className="sr-only">サプライヤー／バイヤー</legend>
              <div className="grid md:grid-cols-[16rem_1fr] border-b border-border">
                <div
                  aria-hidden="true"
                  className="flex items-center bg-textbg px-5 py-4 text-left md:border-r md:border-border"
                >
                  サプライヤー／バイヤー
                </div>
                <div className="flex gap-4 p-5.5">
                  <div className="flex items-center gap-1">
                    <input
                      type="radio"
                      name="role"
                      id="supplier"
                      value="SUPPLIER"
                      required
                      checked={formData.role === 'SUPPLIER'}
                      onChange={() =>
                        setFormData((prev) => ({
                          ...prev,
                          role: 'SUPPLIER',
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
                      value="BUYER"
                      checked={formData.role === 'BUYER'}
                      onChange={() =>
                        setFormData((prev) => ({ ...prev, role: 'BUYER' }))
                      }
                    />
                    <label htmlFor="buyer" className="text-base">
                      バイヤー
                    </label>
                  </div>
                </div>
              </div>
            </fieldset>

            <FormRow label="事業者名" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full"
                onChange={handleChange}
                value={formData.businessName}
                required
                maxLength={255}
              />
            </FormRow>
            <FormRow label="事業者名カナ" htmlFor="businessNameKana">
              <input
                id="businessNameKana"
                name="businessNameKana"
                type="text"
                className="w-full"
                onChange={handleChange}
                value={formData.businessNameKana}
                required
                maxLength={255}
              />
            </FormRow>
            <FormRow label="郵便番号" htmlFor="businessPostalCode">
              <input
                id="businessPostalCode"
                name="businessPostalCode"
                type="text"
                className="w-full max-w-28"
                onChange={handleChange}
                value={formData.businessPostalCode}
                required
                inputMode="numeric"
                autoComplete="postal-code"
                maxLength={7}
                pattern="[0-9]{7}"
              />
              <p>（ハイフンなし）</p>
            </FormRow>
            <FormRow
              label="事業者住所（都道府県）"
              htmlFor="businessAddressPrefecture"
            >
              <input
                id="businessAddressPrefecture"
                name="businessAddressPrefecture"
                type="text"
                className="w-full"
                onChange={handleChange}
                value={formData.businessAddressPrefecture}
                required
                maxLength={50}
              />
            </FormRow>
            <FormRow
              label="事業者住所（市区町村名・町名・番地）"
              htmlFor="businessAddressMunicipalityStreet"
            >
              <input
                id="businessAddressMunicipalityStreet"
                name="businessAddressMunicipalityStreet"
                type="text"
                className="w-full"
                onChange={handleChange}
                value={formData.businessAddressMunicipalityStreet}
                required
                maxLength={255}
              />
            </FormRow>
            <FormRow label="建物名（任意）" htmlFor="businessAddressBuilding">
              <input
                id="businessAddressBuilding"
                name="businessAddressBuilding"
                type="text"
                className="w-full"
                onChange={handleChange}
                value={formData.businessAddressBuilding}
                maxLength={255}
              />
            </FormRow>
            <FormRow label="事業者電話番号" htmlFor="businessPhoneNumber">
              <input
                id="businessPhoneNumber"
                name="businessPhoneNumber"
                type="tel"
                className="w-full max-w-40"
                onChange={handleChange}
                value={formData.businessPhoneNumber}
                required
                maxLength={20}
              />
            </FormRow>
            <FormRow label="WebサイトURL（任意）" htmlFor="businessWebsiteUrl">
              <input
                id="businessWebsiteUrl"
                name="businessWebsiteUrl"
                type="url"
                className="w-full"
                onChange={handleChange}
                value={formData.businessWebsiteUrl}
                maxLength={255}
              />
            </FormRow>

            <fieldset className="m-0 min-w-0 border-0 p-0 md:border-b md:border-border">
              <legend className="sr-only">担当者名</legend>
              <div className="grid md:grid-cols-[16rem_1fr]">
                <div
                  aria-hidden="true"
                  className="flex items-center bg-textbg px-5 py-4 text-left md:border-r md:border-border"
                >
                  担当者名
                </div>
                <div className="grid grid-cols-1 gap-3 p-3 border-b border-border md:border-none md:grid-cols-2">
                  <div className="flex items-center gap-3">
                    <label htmlFor="contactLastName">姓</label>
                    <input
                      id="contactLastName"
                      name="contactLastName"
                      type="text"
                      className="w-full"
                      onChange={handleChange}
                      value={formData.contactLastName}
                      required
                      maxLength={255}
                    />
                  </div>
                  <div className="flex items-center gap-3">
                    <label htmlFor="contactFirstName">名</label>
                    <input
                      id="contactFirstName"
                      name="contactFirstName"
                      type="text"
                      className="w-full"
                      onChange={handleChange}
                      value={formData.contactFirstName}
                      required
                      maxLength={255}
                    />
                  </div>
                </div>
              </div>
            </fieldset>

            <fieldset className="m-0 min-w-0 border-0 p-0 md:border-b md:border-border">
              <legend className="sr-only">担当者名カナ</legend>
              <div className="grid md:grid-cols-[16rem_1fr]">
                <div
                  aria-hidden="true"
                  className="flex items-center bg-textbg px-5 py-4 text-left md:border-r md:border-border"
                >
                  担当者名カナ
                </div>
                <div className="grid grid-cols-1 gap-3 p-3 border-b border-border md:border-none md:grid-cols-2">
                  <div className="flex items-center gap-3">
                    <label
                      htmlFor="contactLastNameKana"
                      className="whitespace-nowrap"
                    >
                      セイ
                    </label>
                    <input
                      id="contactLastNameKana"
                      name="contactLastNameKana"
                      type="text"
                      className="w-full"
                      onChange={handleChange}
                      value={formData.contactLastNameKana}
                      required
                      maxLength={255}
                    />
                  </div>
                  <div className="flex items-center gap-3">
                    <label
                      htmlFor="contactFirstNameKana"
                      className="whitespace-nowrap"
                    >
                      メイ
                    </label>
                    <input
                      id="contactFirstNameKana"
                      name="contactFirstNameKana"
                      type="text"
                      className="w-full"
                      onChange={handleChange}
                      value={formData.contactFirstNameKana}
                      required
                      maxLength={255}
                    />
                  </div>
                </div>
              </div>
            </fieldset>

            <FormRow label="担当者電話番号" htmlFor="contactPhoneNumber">
              <input
                id="contactPhoneNumber"
                name="contactPhoneNumber"
                type="tel"
                className="w-full max-w-40"
                onChange={handleChange}
                value={formData.contactPhoneNumber}
                required
                maxLength={20}
              />
            </FormRow>

            <div className="grid md:grid-cols-[16rem_1fr]">
              <div className="flex items-center bg-textbg px-5 py-4 md:border-r md:border-border">
                担当者メールアドレス
              </div>
              <p className="p-5.5 text-left">{email}</p>
            </div>            
          </div>
          <p className='text-left'>※事業者名、事業者住所、建物名、WebサイトURLは、事業者情報として公開されます。</p>
          <button
            type="submit"
            className="h-9 w-45 mx-auto mt-16 rounded-full button-base button-form"
            disabled={isSubmitting}
          >
            {isSubmitting ? '送信中...' : '送信する'}
          </button>
          {error && <p className="py-2 text-center text-error">{error}</p>}
        </form>
      </div>
    </>
  )
}

export default BusinessProfileSetupPage
