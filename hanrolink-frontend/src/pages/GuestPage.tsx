import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import logo from '../assets/HanroLink_logo.png'
import mainVisual from '../assets/mainvisual.png'
import GuestCard from '../components/GuestCard'
import type { GuestData } from '../features/guest/guestTypes'
import { getGuestData } from '../features/guest/guestService'

function GuestPage() {
  const [productsData, setProductsData] = useState<GuestData>()
  const [error, setError] = useState('')

  useEffect(() => {
    let isCancelled = false

    const loadGuestData = async () => {
      try {
        const result = await getGuestData()

        if (!isCancelled) {
          setProductsData(result)
        }
      } catch (error: unknown) {
        if (!isCancelled) {
          setError(
            error instanceof Error
              ? error.message
              : '商品情報の取得に失敗しました。',
          )
        }
      }
    }
    void loadGuestData()

    return () => {
      isCancelled = true
    }
  }, [])

  const renderProductList = () => {
    if (error) {
      return (
        <p role="alert" className="py-10 text-center text-error">
          {error}
        </p>
      )
    }

    if (!productsData) {
      return <p className="py-10 text-center">読み込み中...</p>
    }

    if (productsData.length === 0) {
      return <p className="py-10 text-center">商品情報が登録されていません</p>
    }

    return (
      <div className="mx-auto grid max-w-300 grid-cols-1 justify-items-center gap-7 px-4 md:grid-cols-2 xl:grid-cols-3">
        {productsData.map((product, index) => (
          <GuestCard
            // バックエンドからのデータにidが追加されたら、keyをindexからidに変える
            key={index}
            name={product.name}
            supplierName={product.supplierBusinessName}
            mainImageUrl={product.mainImageUrl}
          />
        ))}
      </div>
    )
  }

  return (
    <>
      <section className="relative overflow-hidden bg-bg max-w-300 mx-auto flex flex-col md:block md:h-100">
        {/* ロゴとボタン */}
        <div className="relative z-20 flex w-full flex-col items-center justify-center gap-4 px-6 py-8 md:h-full md:w-[42%] md:py-0">
          <img
            src={logo}
            alt="HanroLinkのロゴ"
            className="w-full max-w-72 md:w-85"
          />
          <div className="flex flex-wrap justify-center gap-4 md:gap-12">
            <Link
              to="/signup"
              className="flex h-9 w-30 items-center justify-center rounded-full border border-accent bg-accent text-bg"
            >
              新規登録
            </Link>

            <Link
              to="/login"
              className="flex h-9 w-30 items-center justify-center rounded-full border border-accent bg-bg text-accent"
            >
              ログイン
            </Link>
          </div>
          {/* ヘッダーを出す・しまう目標 */}
          <span
            id="guest-header-trigger"
            aria-hidden="true"
            className="block h-px w-full md:absolute md:bottom-0 md:left-0"
          ></span>
        </div>
        {/* メインビジュアル */}
        <img
          src={mainVisual}
          alt="メインビジュアルのいちごジャム"
          className="h-60 w-full object-cover object-center md:absolute md:inset-y-0 md:right-0 md:h-full md:w-[73%]"
        />
        {/* ロゴのバック */}
        <div
          aria-hidden="true"
          className="absolute inset-y-0 left-0 z-10 hidden w-[55%] bg-linear-to-r from-bg via-bg/95 to-transparent md:block"
        />
      </section>
      {/* ここから商品一覧 */}
      <div className="max-w-300 mx-auto">
        <p className="mb-8">
          売り手と買い手のタイミングをマッチするHanroLinkへようこそ&#xFF01;&#xFF01;
        </p>
        {renderProductList()}
      </div>
    </>
  )
}

export default GuestPage
