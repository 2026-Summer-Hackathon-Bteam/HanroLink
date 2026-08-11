import { useEffect, useState, type SubmitEvent } from 'react'
import type {
  StoryFormChanges,
  StoryFormData,
  ProductInformationFormData,
  MonthlySupplyCapacityFormData,
  SupplierProductFormOptions,
} from '../features/product/productFormTypes'
import ProductStoryFieldset from '../features/product/components/ProductStoryFieldset'
import FormRow from '../components/FormRow'
import { getProductFormOptions } from '../features/product/productFormService'
import {
  createTargetMonths,
  formatTargetMonth,
} from '../shared/utils/yearMonth'

function ProductCreatePage() {
  const [stories, setStories] = useState<StoryFormData[]>([
    {
      position: 1,
      productStorySectionTemplateId: '',
      imageFile: null,
      body: '',
    },
    {
      position: 2,
      productStorySectionTemplateId: '',
      imageFile: null,
      body: '',
    },
    {
      position: 3,
      productStorySectionTemplateId: '',
      imageFile: null,
      body: '',
    },
    {
      position: 4,
      productStorySectionTemplateId: '',
      imageFile: null,
      body: '',
    },
  ])
  const [productInformations, setProductInformations] =
    useState<ProductInformationFormData>({
      name: '',
      productCategoryId: '',
      mainIngredientRegionId: '',
      mainImageFile: null,
      contentQuantity: '',
      expirationType: '',
      shelfLifeDays: '',
      storageType: '',
      desiredRetailPrice: '',
      allergyInformation: '',
      certificationInformation: '',
      caseSize: '',
      unitsPerCase: '',
      minimumOrderQuantity: '',
      shippingLeadTimeDays: '',
      salesAreaRestriction: '',
    })
  const [productFormOptions, setProductFormOptions] =
    useState<SupplierProductFormOptions | null>(null)
  const [selectedProductCategoryGroupId, setSelectedProductCategoryGroupId] =
    useState<number | ''>('')
  const [monthlySupplyCapacities, setMonthlySupplyCapacities] = useState<
    MonthlySupplyCapacityFormData[]
  >(() => 
    createTargetMonths().map((targetMonth) => ({
      targetMonth,
      availableQuantity: '',
    }))
)
  const [error, setError] = useState('')

  useEffect(() => {
    let isCancelled = false

    const loadProductFormOptions = async () => {
      try {
        const result = await getProductFormOptions()

        if (!isCancelled) {
          setProductFormOptions(result)
        }
      } catch {
        if (!isCancelled) {
          setError(
            'ストーリーのテンプレートおよびフォーム選択肢の取得に失敗しました。',
          )
        }
      }
    }

    void loadProductFormOptions()

    return () => {
      isCancelled = true
    }
  }, [])

  const handleStoryChange = (position: number, changes: StoryFormChanges) => {
    setStories((prev) =>
      prev.map((story) =>
        story.position === position ? { ...story, ...changes } : story,
      ),
    )
  }

  const handleMonthlySupplyCapacityChange = (
    targetMonth: string,
    value: string,
  ) => {
    setMonthlySupplyCapacities((previous) =>
      previous.map((capacity) =>
        capacity.targetMonth === targetMonth
          ? {
              ...capacity,
              availableQuantity: value === '' ? '' : Number(value),
            }
          : capacity,
      ),
    )
  }

  const handleSubmit = (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()
    // 入力内容の検証と送信処理を書く
  }

  if (error) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {error}
      </p>
    )
  }

  if (!productFormOptions) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  return (
    <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
      <h2>商品登録</h2>
      <p className="mb-8">
        商品情報と一緒に４枚の写真を使って、バイヤーにアピールしましょう！
        <br />
        タイトルを選択すると写真や文章のヒントが表示されます
        <br />
        商品の魅力に合うものを選んで、商品のこだわりを伝えてください。
      </p>
      <form className="flex flex-col mx-auto" onSubmit={handleSubmit}>
        <h3 className="text-start pl-1">商品ストーリー</h3>
        <div className="[&>fieldset:not(:first-child)>legend]:border-t-0 mb-8">
          {stories.map((story) => (
            <ProductStoryFieldset
              key={story.position}
              story={story}
              templates={productFormOptions.productStorySectionTemplates}
              onChange={handleStoryChange}
            />
          ))}
        </div>

        <h3 className="text-start pl-1">商品情報</h3>
        <div className="overflow-hidden border border-border divide-y divide-border">
          <FormRow label="商品写真" htmlFor="mainImageFile">
            <div className="w-full flex flex-col items-start gap-2 min-w-0">
              <p className="text-start">
                商品のメイン画像、サムネイル画像として使用されます。
              </p>
              <input
                id="mainImageFile"
                name="mainImageFile"
                type="file"
                accept="image/png,image/jpeg,image/webp"
                className="file:text-bg file:rounded-full file:bg-border file:px-4 file:py-2 min-w-0 w-full max-w-full"
                onChange={(e) => {
                  setProductInformations((prev) => ({
                    ...prev,
                    mainImageFile: e.target.files?.[0] ?? null,
                  }))
                }}
                required
              />
            </div>
          </FormRow>

          <FormRow label="商品名" htmlFor="name">
            <input
              id="name"
              name="name"
              type="text"
              value={productInformations.name}
              onChange={(e) => {
                setProductInformations((prev) => ({
                  ...prev,
                  name: e.target.value,
                }))
              }}
              required
              maxLength={255}
              className="w-full"
            />
          </FormRow>

          <FormRow label="商品カテゴリー" htmlFor="productCategoryGroupId">
            <div className="flex flex-col gap-2 w-full min-w-0">
              <div className="flex flex-col items-start md:flex-row md:items-center md:gap-2">
                <label
                  htmlFor="productCategoryGroupId"
                  className="shrink-0 whitespace-nowrap"
                >
                  カテゴリーグループ：
                </label>
                <select
                  id="productCategoryGroupId"
                  name="productCategoryGroupId"
                  value={selectedProductCategoryGroupId}
                  onChange={(e) => {
                    const value = e.target.value
                    setSelectedProductCategoryGroupId(
                      value === '' ? '' : Number(value),
                    )
                    setProductInformations((prev) => ({
                      ...prev,
                      productCategoryId: '',
                    }))
                  }}
                  required
                  className="h-11 w-full rounded-lg border-[0.5px] border-text px-3 shadow-sm md:w-80"
                >
                  <option value="">選択してください</option>
                  {[...productFormOptions.productCategoryGroups]
                    .sort((a, b) => a.sortOrder - b.sortOrder)
                    .map((option) => (
                      <option key={option.id} value={option.id}>
                        {option.name}
                      </option>
                    ))}
                </select>
              </div>
              <div className="flex flex-col items-start md:flex-row md:items-center md:gap-2">
                <label
                  htmlFor="productCategoryId"
                  className="shrink-0 whitespace-nowrap"
                >
                  商品カテゴリー：
                </label>
                <select
                  id="productCategoryId"
                  name="productCategoryId"
                  value={productInformations.productCategoryId}
                  onChange={(e) => {
                    const value = e.target.value
                    setProductInformations((prev) => ({
                      ...prev,
                      productCategoryId: value === '' ? '' : Number(value),
                    }))
                  }}
                  disabled={selectedProductCategoryGroupId === ''}
                  required
                  className="h-11 w-full rounded-lg border-[0.5px] border-text px-3 shadow-sm md:w-80 disabled:cursor-not-allowed disabled:opacity-50"
                >
                  <option value="">
                    {selectedProductCategoryGroupId === ''
                      ? '先にカテゴリーグループを選択してください'
                      : '選択してください'}
                  </option>
                  {[...productFormOptions.productCategories]
                    .filter(
                      (option) =>
                        option.productCategoryGroupId ===
                        selectedProductCategoryGroupId,
                    )
                    .sort((a, b) => a.sortOrder - b.sortOrder)
                    .map((option) => (
                      <option key={option.id} value={option.id}>
                        {option.name}
                      </option>
                    ))}
                </select>
              </div>
            </div>
          </FormRow>

          <FormRow label="賞味期限／消費期限" htmlFor="expirationType">
            <select
              id="expirationType"
              name="expirationType"
              value={productInformations.expirationType}
              onChange={(e) => {
                const selectedOption =
                  productFormOptions.productExpirationTypes.find(
                    (option) => option.value === e.target.value,
                  )
                const expirationType = selectedOption?.value ?? ''

                setProductInformations((prev) => ({
                  ...prev,
                  expirationType,
                  shelfLifeDays:
                    expirationType === 'NOT_APPLICABLE' || expirationType === ''
                      ? ''
                      : prev.shelfLifeDays,
                }))
              }}
              required
              className="h-11 w-full md:w-1/3 rounded-lg border-[0.5px] border-text px-3 shadow-sm"
            >
              <option value="">選択してください</option>
              {productFormOptions.productExpirationTypes.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </FormRow>

          <FormRow label="賞味期限／消費期限&emsp;日数" htmlFor="shelfLifeDays">
            <input
              id="shelfLifeDays"
              name="shelfLifeDays"
              type="number"
              value={productInformations.shelfLifeDays}
              onChange={(e) => {
                const value = e.target.value
                setProductInformations((prev) => ({
                  ...prev,
                  shelfLifeDays: value === '' ? '' : Number(value),
                }))
              }}
              required={
                productInformations.expirationType === 'BEST_BEFORE' ||
                productInformations.expirationType === 'USE_BY'
              }
              min={1}
              disabled={
                productInformations.expirationType === 'NOT_APPLICABLE' ||
                productInformations.expirationType === ''
              }
              className="w-full md:w-1/3"
            />
          </FormRow>

          <FormRow label="主原料産地" htmlFor="mainIngredientRegionId">
            <select
              id="mainIngredientRegionId"
              name="mainIngredientRegionId"
              value={productInformations.mainIngredientRegionId}
              onChange={(e) => {
                const value = e.target.value
                setProductInformations((prev) => ({
                  ...prev,
                  mainIngredientRegionId: value === '' ? '' : Number(value),
                }))
              }}
              required
              className="h-11 w-full md:w-1/3 rounded-lg border-[0.5px] border-text px-3 shadow-sm"
            >
              <option value="">選択してください</option>
              {[...productFormOptions.mainIngredientRegions]
                .sort((a, b) => a.sortOrder - b.sortOrder)
                .map((option) => (
                  <option key={option.id} value={option.id}>
                    {option.name}
                  </option>
                ))}
            </select>
          </FormRow>

          <FormRow label="内容量" htmlFor="contentQuantity">
            <input
              id="contentQuantity"
              name="contentQuantity"
              type="text"
              value={productInformations.contentQuantity}
              onChange={(e) => {
                setProductInformations((prev) => ({
                  ...prev,
                  contentQuantity: e.target.value,
                }))
              }}
              required
              maxLength={255}
              className="w-full md:w-1/3"
            />
          </FormRow>

          <FormRow label="希望小売価格" htmlFor="desiredRetailPrice">
            <input
              id="desiredRetailPrice"
              name="desiredRetailPrice"
              type="number"
              value={productInformations.desiredRetailPrice}
              onChange={(e) => {
                const value = e.target.value
                setProductInformations((prev) => ({
                  ...prev,
                  desiredRetailPrice: value === '' ? '' : Number(value),
                }))
              }}
              required
              min={1}
              className="w-full md:w-1/3"
            />
          </FormRow>

          <FormRow label="保存方法" htmlFor="storageType">
            <select
              id="storageType"
              name="storageType"
              value={productInformations.storageType}
              onChange={(e) => {
                const selectedStorageType =
                  productFormOptions.storageTypes.find(
                    (type) => type.value === e.target.value,
                  )
                setProductInformations((prev) => ({
                  ...prev,
                  storageType: selectedStorageType
                    ? selectedStorageType.value
                    : '',
                }))
              }}
              required
              className="h-11 w-full md:w-1/3 rounded-lg border-[0.5px] border-text px-3 shadow-sm"
            >
              <option value="">選択してください</option>
              {productFormOptions.storageTypes.map((type) => (
                <option key={type.value} value={type.value}>
                  {type.label}
                </option>
              ))}
            </select>
          </FormRow>

          <FormRow label="１ケースあたり入数（任意）" htmlFor="unitsPerCase">
            <input
              id="unitsPerCase"
              name="unitsPerCase"
              type="number"
              value={productInformations.unitsPerCase}
              onChange={(e) => {
                const value = e.target.value
                setProductInformations((prev) => ({
                  ...prev,
                  unitsPerCase: value === '' ? '' : Number(value),
                }))
              }}
              min={1}
              className="w-full md:w-1/3"
            />
          </FormRow>

          <FormRow
            label="発注リードタイム（任意）"
            htmlFor="shippingLeadTimeDays"
          >
            <input
              id="shippingLeadTimeDays"
              name="shippingLeadTimeDays"
              type="number"
              value={productInformations.shippingLeadTimeDays}
              onChange={(e) => {
                const value = e.target.value
                setProductInformations((prev) => ({
                  ...prev,
                  shippingLeadTimeDays: value === '' ? '' : Number(value),
                }))
              }}
              min={1}
              className="w-full md:w-1/3"
            />
          </FormRow>

          <FormRow label="最低納品数量（任意）" htmlFor="minimumOrderQuantity">
            <input
              id="minimumOrderQuantity"
              name="minimumOrderQuantity"
              type="number"
              value={productInformations.minimumOrderQuantity}
              onChange={(e) => {
                const value = e.target.value
                setProductInformations((prev) => ({
                  ...prev,
                  minimumOrderQuantity: value === '' ? '' : Number(value),
                }))
              }}
              min={1}
              className="w-full md:w-1/3"
            />
          </FormRow>

          <FormRow label="ケースサイズ（任意）" htmlFor="caseSize">
            <input
              id="caseSize"
              name="caseSize"
              type="text"
              value={productInformations.caseSize}
              onChange={(e) => {
                setProductInformations((prev) => ({
                  ...prev,
                  caseSize: e.target.value,
                }))
              }}
              maxLength={255}
              className="w-full"
            />
          </FormRow>

          <FormRow label="認証等（任意）" htmlFor="certificationInformation">
            <input
              id="certificationInformation"
              name="certificationInformation"
              type="text"
              value={productInformations.certificationInformation}
              onChange={(e) => {
                setProductInformations((prev) => ({
                  ...prev,
                  certificationInformation: e.target.value,
                }))
              }}
              className="w-full"
            />
          </FormRow>

          <FormRow label="アレルギー表示（任意）" htmlFor="allergyInformation">
            <input
              id="allergyInformation"
              name="allergyInformation"
              type="text"
              value={productInformations.allergyInformation}
              onChange={(e) => {
                setProductInformations((prev) => ({
                  ...prev,
                  allergyInformation: e.target.value,
                }))
              }}
              maxLength={255}
              className="w-full"
            />
          </FormRow>

          <FormRow
            label="販売エリアの制限（任意）"
            htmlFor="salesAreaRestriction"
          >
            <input
              id="salesAreaRestriction"
              name="salesAreaRestriction"
              type="text"
              value={productInformations.salesAreaRestriction}
              onChange={(e) => {
                setProductInformations((prev) => ({
                  ...prev,
                  salesAreaRestriction: e.target.value,
                }))
              }}
              maxLength={255}
              className="w-full"
            />
          </FormRow>
        </div>

        <h3 className="mt-8 text-start pl-1">商品提供情報</h3>

        <div className="overflow-hidden border border-border">
          {/* スマホ・タブレット用の見出し */}
          <div className="grid grid-cols-[8rem_minmax(0,1fr)] border-b border-border bg-textbg lg:hidden">
            <div className="border-r border-border px-3 py-2 text-left">
              提供可能月
            </div>
            <div className="px-3 py-2 text-left">提供可能数量</div>
          </div>

          <div className="lg:grid lg:grid-cols-[16rem_repeat(6,minmax(0,1fr))]">
            {/* PC用の左側の項目名 */}
            <div className="hidden bg-textbg lg:grid lg:grid-rows-2">
              <div className="flex items-center border-b border-border px-5 py-4 text-left">
                提供可能月
              </div>
              <div className="flex items-center px-5 py-4 text-left">
                提供可能数量
              </div>
            </div>

            {monthlySupplyCapacities.map((capacity) => {
              const inputId = `availableQuantity-${capacity.targetMonth}`

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
                  <label
                    htmlFor={inputId}
                    className="
                      flex items-center bg-textbg px-3 py-3 text-left
                      border-r border-border
                      lg:justify-center lg:bg-bg
                      lg:border-r-0 lg:border-b
                    "
                  >
                    {formatTargetMonth(capacity.targetMonth)}
                  </label>

                  <div className="flex items-center p-3">
                    <input
                      id={inputId}
                      name={inputId}
                      type="number"
                      min={1}
                      required
                      value={capacity.availableQuantity}
                      onChange={(event) => {
                        handleMonthlySupplyCapacityChange(
                          capacity.targetMonth,
                          event.target.value,
                        )
                      }}
                      className="w-full text-right"
                      aria-label={`${formatTargetMonth(capacity.targetMonth)}の提供可能数量`}
                    />
                  </div>
                </div>
              )
            })}
          </div>
        </div>
        <button
          type="submit"
          className="h-9 w-45 mx-auto mt-16 rounded-full border border-accent bg-accentbg"
        >
          登録する
        </button>
      </form>
    </div>
  )
}

export default ProductCreatePage
