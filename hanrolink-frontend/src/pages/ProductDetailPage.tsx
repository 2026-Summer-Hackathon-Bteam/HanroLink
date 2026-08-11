import { useState, useEffect } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import type { ProductDetail } from '../features/product/productDetailTypes'
import { getProductDetailData } from '../features/product/productDetailService'
import { formatTargetMonth } from '../shared/utils/yearMonth'
import mainVisual from '../assets/mainvisual.png'
import DataRow from '../components/DataRow'

function ProductDetailPage() {
  const [productDetailData, setProductDetailData] =
    useState<ProductDetail | null>(null)
  const navigate = useNavigate()
  const [error, setError] = useState('')

  useEffect(() => {
    let isCancelled = false

    const loadProductDetailData = async () => {
      try {
        const result = await getProductDetailData(1)

        if (!isCancelled) {
          setProductDetailData(result)
        }
      } catch {
        if (!isCancelled) {
          setError('商品情報詳細データの取得に失敗しました。')
        }
      }
    }

    void loadProductDetailData()

    return () => {
      isCancelled = true
    }
  }, [])

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
  .filter(capacity => capacity.availableQuantity > 0)
  .map(capacity=> {
    const [,month] = capacity.targetMonth.split('-')
    return `${Number(month)}月`
  }).join('、')

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
          className="rounded-full bg-accent px-5 py-2 text-bg"
        >
          {productDetailData.hidden ? '公開する' : '非表示にする'}
        </button>

        <button
          type="button"
          className="rounded-full border border-error px-5 py-2 text-error"
        >
          削除する
        </button>
      </div>
    </div>
  </section>
)}

      <div className="mb-12">
        <h2 className="mb-0!">{productDetailData.name}</h2>
        <p>{productDetailData.supplier.businessName}</p>
        <p>
          カテゴリー：{productDetailData.productCategory.name}&nbsp;
          &#47;&emsp;地域：{productDetailData.mainIngredientRegion.name}&nbsp;
          &#47;&emsp;提供時期：{availableMonths}
        </p>
      </div>
      {/* ストーリーセクション */}
      <section className="mb-20">
        <article className="flex flex-col mb-6 md:flex-row">
          <div className="aspect-4/3 w-full shrink-0 overflow-hidden md:w-[38%]">
            <img src={productDetailData.productStories[0].imageUrl} className="h-full w-full object-cover" alt='ストーリー画像１'/>
          </div>
          <div className="min-w-0 md:w-[45%]">
            <h3 className="mt-5 border-b border-border pb-2 text-left text-2xl font-bold text-accent textaccent pl-2 pr-4">
              {productDetailData.productStories[0].sectionTitle}
            </h3>
            <p className="mt-3 whitespace-pre-wrap text-left leading-7 pl-2 pr-4">
              {productDetailData.productStories[0].body}
            </p>
          </div>
        </article>

        <article className="flex flex-col mb-6 md:flex-row-reverse">
          <div className="aspect-4/3 w-full shrink-0 overflow-hidden md:w-[38%]">
            <img src={productDetailData.productStories[0].imageUrl} className="h-full w-full object-cover" alt='ストーリー画像２'/>
          </div>
          <div className="min-w-0 md:w-[45%]">
            <h3 className="mt-5 border-b border-border pb-2 text-right text-2xl font-bold text-accent textaccent pl-4 pr-2">
              {productDetailData.productStories[1].sectionTitle}
            </h3>
            <p className="mt-3 whitespace-pre-wrap text-left leading-7 pl-4 pr-2">
              {productDetailData.productStories[1].body}
            </p>
          </div>
        </article>
      </section>
      {/* 商品概要 */}
      <section className="mb-20 flex flex-col items-center gap-4 lg:flex-row">
        <div className="aspect-4/3 w-full shrink-0 overflow-hidden md:w-[42%]">
          <img src={mainVisual} className="h-full w-full object-cover" />
        </div>
        <div>
          <p className="text-left">{productDetailData.supplier.businessName}</p>
          <h3 className="text-2xl text-left mb-3">{productDetailData.name}</h3>
          <div className="flex gap-2 mb-3">
            <span className="px-3 py-1 rounded-full bg-badgearea">
              {productDetailData.mainIngredientRegion.name}
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
          <button
            type="button"
            className="h-9 w-45 block mx-auto lg:ml-0 lg:mr-auto mt-8 rounded-full bg-accent text-bg textaccent"
          >
            商談希望を送る
          </button>
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
              {productDetailData.mainIngredientRegion.name}
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
          </dl>
        </div>
      </section>

      <section>
        <h3 className="mb-1 text-left">サプライヤー情報</h3>
        <dl className="flex flex-col mx-auto overflow-hidden border border-border divide-y divide-border">
          <DataRow itemName="会社名">{productDetailData.supplier.businessName}</DataRow>
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
    </div>
  )
}

export default ProductDetailPage
