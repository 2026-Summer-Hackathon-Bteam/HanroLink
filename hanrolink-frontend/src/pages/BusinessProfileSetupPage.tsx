import { useState, type SubmitEvent } from 'react'
import FormRow from '../components/FormRow'

function BusinessProfileSetupPage() {
  const handleSubmit = (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
  }

  return (
    <>
      <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
        <h2>会社情報登録</h2>
        <p className="mb-8">
          ご登録ありがとうございます。
          <br />
          サービスをご利用いただくため、お手数ですが以下の情報をご入力のうえ送信してください。
          <br />
          各事業者様に安心してご利用いただくため、ご入力いただいた情報をもとに審査を行います。
          <br />
          審査には通常3日程度お時間をいただきますので、あらかじめご了承ください。
        </p>
        <form onSubmit={handleSubmit} className="flex flex-col mx-auto">
          <div className="overflow-hidden border border-border divide-y divide-border">
            <FormRow label="会社名" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full"
              />
            </FormRow>
            <FormRow label="会社名カナ" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full"
              />
            </FormRow>
            <FormRow label="郵便番号" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full max-w-28"
              />
            </FormRow>
            <FormRow label="会社住所（都道府県）" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full"
              />
            </FormRow>
            <FormRow
              label="会社住所（市区町村名・町名・番地）"
              htmlFor="businessName"
            >
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full"
              />
            </FormRow>
            <FormRow label="建物名" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full"
              />
            </FormRow>
            <FormRow label="電話番号" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full max-w-40"
              />
            </FormRow>
            <FormRow label="ホームページ" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full"
              />
            </FormRow>
            <FormRow label="担当者名" htmlFor="businessName">
              <div className="flex items-center gap-2">
                <label>姓</label>
                <input
                  id="businessName"
                  name="businessName"
                  type="text"
                  className="w-full"
                />
              </div>
              <div className="flex items-center gap-2">
                <label>名</label>
                <input
                  id="businessName"
                  name="businessName"
                  type="text"
                  className="w-full"
                />
              </div>
            </FormRow>
            <FormRow label="担当者フリガナ" htmlFor="businessName">
              <div className="flex items-center gap-2">
                <label>姓</label>
                <input
                  id="businessName"
                  name="businessName"
                  type="text"
                  className="w-full"
                />
              </div>
              <div className="flex items-center gap-2">
                <label>名</label>
                <input
                  id="businessName"
                  name="businessName"
                  type="text"
                  className="w-full"
                />
              </div>
            </FormRow>
            <FormRow label="担当者電話番号" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full"
              />
            </FormRow>
            <FormRow label="担当者メールアドレス" htmlFor="businessName">
              <input
                id="businessName"
                name="businessName"
                type="text"
                className="w-full"
                // valueでバックエンドから取得した値を入れる
              />
            </FormRow>
          </div>
          <button
            type="submit"
            className="h-9 w-45 mx-auto mt-16 rounded-full border border-accent bg-accentbg"
          >
            送信する
          </button>
        </form>
      </div>
    </>
  )
}

export default BusinessProfileSetupPage
