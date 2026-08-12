import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import ProductForm from '../features/product/components/ProductForm'
import { getProductDetailData } from '../features/product/productDetailService'
import type { ProductDetail } from '../features/product/productDetailTypes'
import type {
  ProductFormInitialValues,
  ProductFormValues,
} from '../features/product/productFormTypes'
import { createTargetMonths } from '../shared/utils/yearMonth'

const convertToInitialValues = (
  detail: ProductDetail,
): ProductFormInitialValues => {
  const quantityByMonth = new Map(
    detail.monthlySupplyCapacities.map(
      (capacity) => [capacity.targetMonth, capacity.availableQuantity] as const,
    ),
  )

  return {
    existingMainImageUrl: detail.mainImageUrl,

    productInformations: {
      name: detail.name,
      productCategoryId: detail.productCategory.id,
      mainIngredientRegionId: detail.mainIngredientRegion.id,
      mainImageFile: null,
      contentQuantity: detail.contentQuantity,
      expirationType: detail.productExpirationType.value,
      shelfLifeDays: detail.shelfLifeDays ?? '',
      storageType: detail.storageType.value,
      desiredRetailPrice: detail.desiredRetailPrice,
      allergyInformation: detail.allergyInformation ?? '',
      certificationInformation: detail.certificationInformation ?? '',
      caseSize: detail.caseSize ?? '',
      unitsPerCase: detail.unitsPerCase ?? '',
      minimumOrderQuantity: detail.minimumOrderQuantity ?? '',
      shippingLeadTimeDays: detail.shippingLeadTimeDays ?? '',
      salesAreaRestriction: detail.salesAreaRestriction ?? '',
    },

    stories: [...detail.productStories]
      .sort((a, b) => a.position - b.position)
      .map((story) => ({
        id: story.id,
        position: story.position,
        productStorySectionTemplateId: story.productStorySectionTemplateId,
        imageFile: null,
        existingImageUrl: story.imageUrl,
        body: story.body,
      })),

    monthlySupplyCapacities: createTargetMonths().map((targetMonth) => ({
      targetMonth,
      availableQuantity: quantityByMonth.get(targetMonth) ?? '',
    })),
  }
}

function ProductEditPage() {
  const { productId } = useParams<{ productId: string }>()
  const [initialValues, setInitialValues] =
    useState<ProductFormInitialValues | null>(null)
  const [error, setError] = useState('')
  const parsedProductId = Number(productId)
  const isValidProductId =
    productId !== undefined &&
    Number.isInteger(parsedProductId) &&
    parsedProductId > 0

  useEffect(() => {
    if (!isValidProductId) {
      return
    }

    let isCancelled = false

    const loadProduct = async () => {
      try {
        const detail = await getProductDetailData(parsedProductId)

        if (!isCancelled) {
          setInitialValues(convertToInitialValues(detail))
        }
      } catch {
        if (!isCancelled) {
          setError('商品情報の取得に失敗しました。')
        }
      }
    }

    void loadProduct()

    return () => {
      isCancelled = true
    }
  }, [isValidProductId, parsedProductId])

  const handleUpdate = (values: ProductFormValues) => {
    void values
    // TODO: 商品更新APIを呼ぶ
  }

  if (!isValidProductId) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        商品IDが正しくありません。
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

  if (!initialValues) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  return (
    <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
      <h2>商品情報編集</h2>

      <ProductForm
        mode="edit"
        initialValues={initialValues}
        onSubmit={handleUpdate}
      />
    </div>
  )
}

export default ProductEditPage
