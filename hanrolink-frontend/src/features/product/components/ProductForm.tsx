import { useState, useEffect, type SubmitEvent } from 'react'
import FormRow from '../../../components/FormRow'
import { getProductFormOptions } from '../productFormService'
import type {
  StoryFormChanges,
  StoryFormData,
  ProductInformationFormData,
  MonthlySupplyCapacityFormData,
  SupplierProductFormOptions,
} from '../productFormTypes'
import ProductStoryFieldset from './ProductStoryFieldset'
import { formatTargetMonth } from '../../../shared/utils/yearMonth'
import type { ProductFormProps } from '../productFormTypes'

function ProductForm({
  mode,
  initialValues,
  onSubmit,
  onCancel,
  isSubmitting,
  submitProgress,
  submitError,
}: ProductFormProps) {
  const [stories, setStories] = useState<StoryFormData[]>(initialValues.stories)
  const [productInformations, setProductInformations] =
    useState<ProductInformationFormData>(initialValues.productInformations)
  const [productFormOptions, setProductFormOptions] =
    useState<SupplierProductFormOptions | null>(null)
  const [selectedProductCategoryGroupId, setSelectedProductCategoryGroupId] =
    useState<number | ''>('')
  const [monthlySupplyCapacities, setMonthlySupplyCapacities] = useState<
    MonthlySupplyCapacityFormData[]
  >(initialValues.monthlySupplyCapacities)
  const [formOptionsError, setFormOptionsError] = useState('')

  useEffect(() => {
    let isCancelled = false

    const loadProductFormOptions = async () => {
      try {
        const result = await getProductFormOptions()

        if (!isCancelled) {
          setProductFormOptions(result)

          const selectedCategory = result.productCategories.find(
            (category) =>
              category.id ===
              initialValues.productInformations.productCategoryId,
          )

          setSelectedProductCategoryGroupId(
            selectedCategory?.productCategoryGroupId ?? '',
          )
        }
      } catch (error: unknown) {
        if (!isCancelled) {
          setFormOptionsError(
            error instanceof Error
              ? error.message
              : 'ストーリーのテンプレートおよびフォーム選択肢の取得に失敗しました。',
          )
        }
      }
    }

    void loadProductFormOptions()

    return () => {
      isCancelled = true
    }
  }, [initialValues.productInformations.productCategoryId])

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

  const handleSubmit = async (event: SubmitEvent<HTMLFormElement>) => {
    event.preventDefault()

    await onSubmit({
      productInformations,
      stories,
      monthlySupplyCapacities,
    })
  }

  if (formOptionsError) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {formOptionsError}
      </p>
    )
  }

  if (!productFormOptions) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  return (
    <form className="flex flex-col mx-auto" onSubmit={handleSubmit}>
      <h3 className="text-start pl-1">商品ストーリー</h3>
      <div className="[&>fieldset:not(:first-child)>legend]:border-t-0 mb-8">
        {stories.map((story) => (
          <ProductStoryFieldset
            key={story.position}
            mode={mode}
            story={story}
            templates={productFormOptions.productStorySectionTemplates}
            onChange={handleStoryChange}
          />
        ))}
      </div>

      <h3 className="text-start pl-1">商品情報</h3>
      <div className="overflow-hidden border border-border divide-y divide-border">
        {initialValues.existingMainImageUrl && (
          <FormRow label="現在登録されている画像">
            <img
              src={initialValues.existingMainImageUrl}
              alt={`${productInformations.name}の現在の商品画像`}
              className="aspect-4/3 w-40 rounded-md object-cover"
            />
          </FormRow>
        )}

        <FormRow
          label={mode === 'create' ? '商品写真' : '商品写真を変更'}
          htmlFor="mainImageFile"
        >
          <div className="w-full flex flex-col items-start gap-2 min-w-0">
            <p className="text-start">
              商品のメイン画像、サムネイル画像として使用されます。
            </p>
            <input
              id="mainImageFile"
              name="mainImageFile"
              type="file"
              accept="image/png,image/jpeg,image/webp,image/heic,image/heif"
              className="file:text-bg file:rounded-full file:bg-border file:px-4 file:py-2 min-w-0 w-full max-w-full"
              onChange={(e) => {
                setProductInformations((prev) => ({
                  ...prev,
                  mainImageFile: e.target.files?.[0] ?? null,
                }))
              }}
              required={mode === 'create'}
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
                  .sort((a, b) => a.id - b.id)
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
                  .sort((a, b) => a.id - b.id)
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
          <div className="flex gap-2 items-center w-full">
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
            <p>日</p>
          </div>
        </FormRow>

        <FormRow label="主原料産地" htmlFor="mainIngredientOriginPrefectureId">
          <select
            id="mainIngredientOriginPrefectureId"
            name="mainIngredientOriginPrefectureId"
            value={productInformations.mainIngredientOriginPrefectureId}
            onChange={(e) => {
              const value = e.target.value
              setProductInformations((prev) => ({
                ...prev,
                mainIngredientOriginPrefectureId:
                  value === '' ? '' : Number(value),
              }))
            }}
            required
            className="h-11 w-full md:w-1/3 rounded-lg border-[0.5px] border-text px-3 shadow-sm"
          >
            <option value="">選択してください</option>
            {[...productFormOptions.mainIngredientOriginPrefectures]
              .sort((a, b) => a.id - b.id)
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
          <div className="flex gap-2 items-center w-full">
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
            <p>円</p>
          </div>
        </FormRow>

        <FormRow label="保存方法" htmlFor="storageType">
          <select
            id="storageType"
            name="storageType"
            value={productInformations.storageType}
            onChange={(e) => {
              const selectedStorageType = productFormOptions.storageTypes.find(
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
          <div className="flex gap-2 items-center w-full">
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
            <p>日</p>
          </div>
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
            maxLength={1000}
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
                    min={0}
                    required
                    value={capacity.availableQuantity}
                    onChange={(event) => {
                      handleMonthlySupplyCapacityChange(
                        capacity.targetMonth,
                        event.target.value,
                      )
                    }}
                    className="w-full text-left"
                    aria-label={`${formatTargetMonth(capacity.targetMonth)}の提供可能数量`}
                  />
                </div>
              </div>
            )
          })}
        </div>
      </div>
      <div className="mt-16 flex justify-center gap-4">
        {mode === 'edit' && onCancel && (
          <button
            type="button"
            onClick={onCancel}
            className="flex h-9 w-45 items-center justify-center rounded-full button-base button-secondary"
            disabled={isSubmitting}
          >
            キャンセル
          </button>
        )}
        <button
          type="submit"
          className="h-9 w-45 rounded-full button-base button-form"
          disabled={isSubmitting}
        >
          {mode === 'create'
            ? isSubmitting
              ? '登録中...'
              : '登録する'
            : '更新する'}
        </button>
      </div>
      {/* ProductFormのボタンの下に進捗とエラーを表示 */}
      {submitProgress && (
        <p role="status" aria-live="polite" className="mb-3 text-center">
          {submitProgress}
        </p>
      )}

      {submitError && (
        <p
          role="alert"
          className="mb-3 whitespace-pre-line text-center text-error"
        >
          {submitError}
        </p>
      )}
    </form>
  )
}

export default ProductForm
