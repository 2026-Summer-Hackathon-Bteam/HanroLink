import { useEffect, useState } from 'react'
import {
  type StoryFormChanges,
  type StoryFormData,
  type ProductInformationFormData,
} from '../features/product/productFormTypes'
import ProductStoryFieldset from '../features/product/components/ProductStoryFieldset'
import FormRow from '../components/FormRow'
import { getProductFormOptions } from '../features/product/productFormService'
import type { SupplierProductFormOptions } from '../features/product/productFormTypes'

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
      <form className="flex flex-col mx-auto">
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
                className="file:text-bg file:rounded-full file:bg-border file:px-4 file:py-2 min-w-0"
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
            />
          </FormRow>

          <FormRow label="商品カテゴリー" htmlFor="productCategoryId">
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
              aria-label="商品カテゴリーグループ"
              required
              className="h-11 w-full md:w-1/3 rounded-lg border-[0.5px] border-text px-3 shadow-sm"
            >
              <option value="">選択してください</option>
              {[...productFormOptions.productCategoryGroups]
                .sort((a, b) => a.sortOrder - b.sortOrder)
                .map((pcg) => (
                  <option key={pcg.id} value={pcg.id}>
                    {pcg.name}
                  </option>
                ))}
            </select>
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
              aria-label="商品カテゴリー"
              required
              className="h-11 w-full md:w-1/3 rounded-lg border-[0.5px] border-text px-3 shadow-sm"
            >
              <option value="">
                {selectedProductCategoryGroupId === ''
                  ? '先にカテゴリーグループを選択してください'
                  : '選択してください'}
              </option>
              {[...productFormOptions.productCategories]
                .filter(
                  (pc) =>
                    pc.productCategoryGroupId ===
                    selectedProductCategoryGroupId,
                )
                .sort((a, b) => a.sortOrder - b.sortOrder)
                .map((pc) => (
                  <option key={pc.id} value={pc.id}>
                    {pc.name}
                  </option>
                ))}
            </select>
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
              {productFormOptions.productExpirationTypes.map((pet) => (
                <option key={pet.value} value={pet.value}>
                  {pet.label}
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
                .map((mir) => (
                  <option key={mir.id} value={mir.id}>
                    {mir.name}
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
            />
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
            />
          </FormRow>
        </div>
      </form>
    </div>
  )
}

export default ProductCreatePage
