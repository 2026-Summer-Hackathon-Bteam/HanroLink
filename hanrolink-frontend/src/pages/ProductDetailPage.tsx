import { useState, useEffect, useRef } from 'react'
import { useNavigate, Link, useParams } from 'react-router-dom'
import type { ProductDetail } from '../features/product/productDetailTypes'
import {
  getProductDetailData,
  deleteProduct,
  updateProductVisibility,
} from '../features/product/productDetailService'
import { formatTargetMonth } from '../shared/utils/yearMonth'
import DataRow from '../components/DataRow'
import ProductStorySection from '../features/product/components/ProductStorySection'
import { createProductNegotiationRequest } from '../features/negotiation/negotiationRequestService'

function ProductDetailPage() {
  const [productDetailData, setProductDetailData] =
    useState<ProductDetail | null>(null)
  const navigate = useNavigate()
  const [error, setError] = useState('')
  const deleteDialogRef = useRef<HTMLDialogElement>(null)
  const [isDeleting, setIsDeleting] = useState(false)
  const [deleteError, setDeleteError] = useState('')
  const [isUpdatingVisibility, setIsUpdatingVisibility] = useState(false)
  const [visibilityError, setVisibilityError] = useState('')
  const { productId } = useParams()
  const negotiationDialogRef = useRef<HTMLDialogElement>(null)
  const [isSubmittingNegotiation, setIsSubmittingNegotiation] = useState(false)
  const [negotiationError, setNegotiationError] = useState('')
  const [negotiationSucceeded, setNegotiationSucceeded] = useState(false)

  useEffect(() => {
    if (!productId) return

    let isCancelled = false

    const loadProductDetailData = async () => {
      try {
        const result = await getProductDetailData(productId)

        if (!isCancelled) {
          setProductDetailData(result)
        }
      } catch (error: unknown) {
        if (!isCancelled) {
          setError(
            error instanceof Error
              ? error.message
              : '商品情報詳細データの取得に失敗しました。',
          )
        }
      }
    }

    void loadProductDetailData()

    return () => {
      isCancelled = true
    }
  }, [productId])

  const handleDelete = async () => {
    if (isDeleting || !productDetailData) return

    setIsDeleting(true)
    setDeleteError('')

    try {
      await deleteProduct(productDetailData.id)
      deleteDialogRef.current?.close()
      navigate('/mypage/supplier')
    } catch (error: unknown) {
      setDeleteError(
        error instanceof Error ? error.message : '商品の削除に失敗しました。',
      )
      setIsDeleting(false)
    }
  }

  const handleVisibilityChange = async () => {
    if (isUpdatingVisibility || !productDetailData) return

    const nextHidden = !productDetailData.hidden

    setIsUpdatingVisibility(true)
    setVisibilityError('')

    try {
      await updateProductVisibility(productDetailData.id, nextHidden)

      setProductDetailData((current) =>
        current ? { ...current, hidden: nextHidden } : current,
      )
    } catch (error: unknown) {
      setVisibilityError(
        error instanceof Error
          ? error.message
          : '商品の公開状態の変更に失敗しました。',
      )
    } finally {
      setIsUpdatingVisibility(false)
    }
  }

  const handleOpenNegotiationDialog = () => {
    if (
      !productDetailData ||
      !productDetailData.permissions.canCreateNegotiationRequest ||
      productDetailData.hasMyActiveNegotiationRequest
    ) {
      return
    }

    setNegotiationError('')
    setNegotiationSucceeded(false)
    negotiationDialogRef.current?.showModal()
  }

  const handleCreateNegotiationRequest = async () => {
    if (
      isSubmittingNegotiation ||
      !productDetailData ||
      !productDetailData.permissions.canCreateNegotiationRequest ||
      productDetailData.hasMyActiveNegotiationRequest
    ) {
      return
    }

    setIsSubmittingNegotiation(true)
    setNegotiationError('')

    try {
      await createProductNegotiationRequest(productDetailData.id)

      setProductDetailData((current) =>
        current ? { ...current, hasMyActiveNegotiationRequest: true } : current,
      )

      setNegotiationSucceeded(true)
    } catch (error: unknown) {
      setNegotiationError(
        error instanceof Error
          ? error.message
          : '商談希望の送信に失敗しました。',
      )
    } finally {
      setIsSubmittingNegotiation(false)
    }
  }

  if (!productId) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        商品IDが取得できませんでした。
      </p>
    )
  }

  if (error) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {error}
      </p>
    )
  }

  if (!productDetailData) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  const availableMonths = productDetailData.monthlySupplyCapacities
    .filter((capacity) => capacity.availableQuantity > 0)
    .map((capacity) => {
      const [, month] = capacity.targetMonth.split('-')
      return `${Number(month)}月`
    })
    .join('、')

  return (
    <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
      {productDetailData.permissions.canManage && (
        <section
          aria-labelledby="product-management-title"
          className="mt-6 border-y border-dashed border-border bg-textbg/30 px-4 py-4"
        >
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <h3
              id="product-management-title"
              className="text-left text-base font-bold"
            >
              商品管理
            </h3>

            <div className="flex flex-wrap gap-3">
              <Link
                to={`/products/${productDetailData.id}/edit`}
                className="rounded-full bg-accent px-5 py-2 text-bg"
              >
                編集する
              </Link>

              <button
                type="button"
                className={
                  productDetailData.hidden
                    ? 'rounded-full bg-accent px-5 py-2 text-bg disabled:cursor-not-allowed disabled:opacity-50'
                    : 'rounded-full border border-error px-5 py-2 disabled:cursor-not-allowed disabled:opacity-50'
                }
                onClick={handleVisibilityChange}
                disabled={isUpdatingVisibility}
              >
                {isUpdatingVisibility
                  ? '変更中...'
                  : productDetailData.hidden
                    ? '公開する'
                    : '非表示にする'}
              </button>

              <button
                type="button"
                className="rounded-full border border-error px-5 py-2 text-error"
                onClick={() => deleteDialogRef.current?.showModal()}
              >
                削除する
              </button>
            </div>
            {visibilityError && (
              <p role="alert" className="mt-3 text-left text-sm text-error">
                {visibilityError}
              </p>
            )}
          </div>
        </section>
      )}

      {productDetailData.hidden && productDetailData.permissions.canManage && (
        <div
          role="status"
          className="mt-6 border-y border-dashed border-border bg-textbg/40 px-4 py-3 text-left"
        >
          <p className="font-bold text-accent">この商品は現在非表示です</p>
          <p className="mt-1 text-sm">
            バイヤーの商品検索や商品一覧には表示されません。
            上の「公開する」ボタンから再公開できます。
          </p>
        </div>
      )}

      <div className="mb-12">
        <h2 className="mb-0!">{productDetailData.name}</h2>
        <p>{productDetailData.supplier.businessName}</p>
        <p>
          カテゴリー：{productDetailData.productCategory.name}&nbsp;
          &#47;&emsp;地域：
          {productDetailData.mainIngredientOriginPrefecture.name}&nbsp;
          &#47;&emsp;提供時期：{availableMonths}
        </p>
      </div>
      {/* ストーリーセクション */}
      <section className="mb-20">
        {[...productDetailData.productStories]
          .sort((a, b) => a.position - b.position)
          .map((story) => (
            <ProductStorySection key={story.id} story={story} />
          ))}
      </section>
      {/* 商品概要 */}
      <section className="mb-20 flex flex-col items-center gap-4 lg:flex-row">
        <div className="aspect-4/3 w-full shrink-0 overflow-hidden md:w-[42%]">
          <img
            src={productDetailData.mainImageUrl}
            className="h-full w-full object-cover"
            alt={`${productDetailData.name}のメイン画像`}
          />
        </div>
        <div>
          <p className="text-left">{productDetailData.supplier.businessName}</p>
          <h3 className="text-2xl text-left mb-3">{productDetailData.name}</h3>
          <div className="flex gap-2 mb-3">
            <span className="px-3 py-1 rounded-full bg-badgearea">
              {productDetailData.mainIngredientOriginPrefecture.name}
            </span>
            <span className="px-3 py-1 rounded-full bg-badgesto">
              {productDetailData.storageType.label}
            </span>
            <span className="px-3 py-1 rounded-full bg-badgecate">
              {productDetailData.productCategory.name}
            </span>
          </div>
          <div className="overflow-hidden border border-border">
            {/* スマホ・タブレット用の見出し */}
            <div className="grid grid-cols-[8rem_minmax(0,1fr)] border-b border-border bg-textbg lg:hidden">
              <div className="border-r border-border px-3 py-2 text-left">
                提供可能月
              </div>
              <div className="px-3 py-2 text-center">提供可能数量</div>
            </div>

            <div className="lg:grid lg:grid-cols-[9rem_repeat(6,minmax(0,1fr))]">
              {/* PC用の左側の項目名 */}
              <div className="hidden bg-textbg lg:grid lg:grid-rows-2">
                <div className="flex items-center border-b border-border px-5 py-4 text-left">
                  提供可能月
                </div>
                <div className="flex items-center px-5 py-4 text-left">
                  提供可能数量
                </div>
              </div>

              {productDetailData.monthlySupplyCapacities.map((capacity) => {
                return (
                  <div
                    key={capacity.targetMonth}
                    className="
                              grid grid-cols-[8rem_minmax(0,1fr)]
                              border-b border-border last:border-b-0
                              lg:grid-cols-1 lg:grid-rows-2
                              lg:border-b-0 lg:border-l
                            "
                  >
                    <div
                      className="
                                flex items-center bg-textbg px-3 py-3 text-left
                                border-r border-border
                                lg:justify-center lg:bg-bg
                                lg:border-r-0 lg:border-b lg:text-center
                              "
                    >
                      {formatTargetMonth(capacity.targetMonth)}
                    </div>

                    <div className="flex justify-center items-center p-3">
                      {capacity.availableQuantity}
                    </div>
                  </div>
                )
              })}
            </div>
          </div>
          <div className="lg:flex lg:items-baseline">
            <button
              type="button"
              className="h-9 w-45 block mx-auto lg:ml-0 lg:mr-auto mt-8 rounded-full bg-accent text-bg textaccent disabled:cursor-not-allowed disabled:opacity-50"
              disabled={
                !productDetailData.permissions.canCreateNegotiationRequest ||
                productDetailData.hasMyActiveNegotiationRequest
              }
              onClick={handleOpenNegotiationDialog}
            >
              商談希望を送る
            </button>
            {productDetailData.hasMyActiveNegotiationRequest ? (
              <p className="pt-2 lg:flex-1 lg:text-left lg:pl-2 lg:pt-0">
                この商品には商談希望を送信済みです。
              </p>
            ) : !productDetailData.permissions.canCreateNegotiationRequest ? (
              <p className="pt-2 lg:flex-1 lg:text-left lg:pl-2 lg:pt-0">
                サプライヤーは商品に商談希望を送ることはできません。
              </p>
            ) : null}
          </div>
        </div>
      </section>

      {/* 商品情報 */}
      <section className="mb-8">
        <h3 className="mb-1 text-left">商品情報</h3>

        <div className="grid grid-cols-1 lg:gap-5 lg:grid-cols-2 lg:items-start">
          <dl className="overflow-hidden border border-border divide-y divide-border">
            <DataRow itemName="商品名">{productDetailData.name}</DataRow>

            <DataRow itemName="商品カテゴリー">
              {productDetailData.productCategory.name}
            </DataRow>

            <DataRow itemName="賞味期限／消費期限">
              {productDetailData.productExpirationType.label}
              {productDetailData.shelfLifeDays
                ? ` ${productDetailData.shelfLifeDays}日`
                : ''}
            </DataRow>

            <DataRow itemName="主原料産地">
              {productDetailData.mainIngredientOriginPrefecture.name}
            </DataRow>

            <DataRow itemName="内容量">
              {productDetailData.contentQuantity}
            </DataRow>

            <DataRow itemName="希望小売価格">
              {productDetailData.desiredRetailPrice}
            </DataRow>

            <DataRow itemName="保存方法">
              {productDetailData.storageType.label}
            </DataRow>
          </dl>

          <dl className="overflow-hidden border border-t-0 border-border divide-y divide-border lg:border-t">
            <DataRow itemName="１ケース当たり入数">
              {productDetailData.unitsPerCase ?? '-'}
            </DataRow>

            <DataRow itemName="発送リードタイム">
              {productDetailData.shippingLeadTimeDays
                ? `${productDetailData.shippingLeadTimeDays}日`
                : '-'}
            </DataRow>

            <DataRow itemName="最低納品数量">
              {productDetailData.minimumOrderQuantity ?? '-'}
            </DataRow>

            <DataRow itemName="ケースサイズ">
              {productDetailData.caseSize ?? '-'}
            </DataRow>

            <DataRow itemName="認証等">
              {productDetailData.certificationInformation ?? '-'}
            </DataRow>

            <DataRow itemName="アレルギー表示">
              {productDetailData.allergyInformation ?? '-'}
            </DataRow>

            <DataRow itemName="販売エリアの制限">
              {productDetailData.salesAreaRestriction ?? '-'}
            </DataRow>
          </dl>
        </div>
      </section>

      <section>
        <h3 className="mb-1 text-left">サプライヤー情報</h3>
        <dl className="flex flex-col mx-auto overflow-hidden border border-border divide-y divide-border">
          <DataRow itemName="会社名">
            {productDetailData.supplier.businessName}
          </DataRow>
          <DataRow itemName="会社住所">
            {[
              productDetailData.supplier.businessAddressPrefecture,
              productDetailData.supplier.businessAddressMunicipalityStreet,
              productDetailData.supplier.businessAddressBuilding,
            ]
              .filter(Boolean)
              .join('')}
          </DataRow>
          <DataRow itemName="ホームページ">
            {productDetailData.supplier.businessWebsiteUrl ? (
              <a
                href={productDetailData.supplier.businessWebsiteUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="text-other underline"
              >
                {productDetailData.supplier.businessWebsiteUrl}
              </a>
            ) : (
              '-'
            )}
          </DataRow>
        </dl>
      </section>

      <button
        type="button"
        onClick={() => navigate(-1)}
        className="mx-auto mt-16 h-9 w-45 rounded-full border border-accent bg-accentbg"
      >
        前のページに戻る
      </button>

      {/* 商談希望確認モーダル */}
      <dialog
        ref={negotiationDialogRef}
        aria-labelledby="create-negotiation-request-title"
        aria-describedby="create-negotiation-request-description"
        className="m-auto w-[min(90vw,32rem)] rounded-lg border-0 bg-bg p-6 shadow-xl backdrop:bg-black/50"
      >
        {negotiationSucceeded ? (
          // 送信成功時
          <>
            <h3
              id="create-negotiation-request-title"
              className="text-lg font-bold"
            >
              商談希望を送信しました
            </h3>

            <p
              id="create-negotiation-request-description"
              role="status"
              className="mt-4"
            >
              「{productDetailData.name}」への商談希望を送信しました。
            </p>

            <div className="mt-6 flex justify-center">
              <button
                type="button"
                className="rounded-full bg-accent px-5 py-2 text-bg"
                onClick={() => negotiationDialogRef.current?.close()}
              >
                閉じる
              </button>
            </div>
          </>
        ) : (
          // 送信前
          <>
            <h3
              id="create-negotiation-request-title"
              className="text-lg font-bold"
            >
              商談希望を送りますか？
            </h3>

            <p id="create-negotiation-request-description" className="mt-4">
              {productDetailData.supplier.businessName}の「
              {productDetailData.name}」に商談希望を送ります。
            </p>

            <ul className="mt-4 list-disc space-y-2 pl-5 text-left text-sm">
              <li>商談希望を送ると、相手のマイページに表示されます。</li>
              <li>
                相手が商談を開始すると、メッセージをやり取りするためのチャットが作成されます。
              </li>
              <li>
                商談希望の送信は、取引条件への同意または契約成立を意味するものではありません。
              </li>
            </ul>

            {negotiationError && (
              <p role="alert" className="mt-4 text-center text-error">
                {negotiationError}
              </p>
            )}

            <div className="mt-6 flex justify-center gap-3">
              <button
                type="button"
                className="rounded-full border border-accent px-5 py-2 disabled:cursor-not-allowed disabled:opacity-50"
                onClick={() => negotiationDialogRef.current?.close()}
                disabled={isSubmittingNegotiation}
              >
                キャンセル
              </button>

              <button
                type="button"
                className="rounded-full bg-accent px-5 py-2 text-bg disabled:cursor-not-allowed disabled:opacity-50"
                onClick={() => void handleCreateNegotiationRequest()}
                disabled={isSubmittingNegotiation}
              >
                {isSubmittingNegotiation ? '送信中...' : '商談希望を送る'}
              </button>
            </div>
          </>
        )}
      </dialog>

      {/* 商品削除確認モーダル */}
      <dialog
        ref={deleteDialogRef}
        aria-labelledby="delete-product-title"
        className="m-auto w-[min(90vw,28rem)] rounded-lg border-0 bg-bg p-6 shadow-xl backdrop:bg-black/50"
      >
        <h3 id="delete-product-title" className="text-lg font-bold">
          商品を削除しますか？
        </h3>

        <p className="mt-4">「{productDetailData.name}」を削除します。</p>

        <p className="mt-2 text-sm text-error">
          削除した商品は復元できません。
          <br />
          商品を削除すると、関連する商談希望も取り消されます。
          <br />
          この操作は元に戻せません。
        </p>

        <div className="mt-6 flex justify-center gap-3">
          <button
            type="button"
            onClick={() => deleteDialogRef.current?.close()}
            className="rounded-full border border-accent px-5 py-2 disabled:opacity-50 disabled:cursor-not-allowed"
            disabled={isDeleting}
          >
            キャンセル
          </button>

          <button
            type="button"
            className="rounded-full bg-error px-5 py-2 text-bg  disabled:opacity-50 disabled:cursor-not-allowed"
            onClick={handleDelete}
            disabled={isDeleting}
          >
            {isDeleting ? '削除中...' : '削除する'}
          </button>
        </div>
        {deleteError && (
          <p role="alert" className="mt-3 text-sm text-error">
            {deleteError}
          </p>
        )}
      </dialog>
    </div>
  )
}

export default ProductDetailPage
