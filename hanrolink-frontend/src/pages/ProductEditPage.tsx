import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import ProductForm from '../features/product/components/ProductForm'
import { getProductDetailData } from '../features/product/productDetailService'
import type { ProductDetail } from '../features/product/productDetailTypes'
import type {
  ProductFormInitialValues,
  ProductFormValues,
} from '../features/product/productFormTypes'
import { createTargetMonths } from '../shared/utils/yearMonth'
import {
  updateProduct,
  uploadPreparedProductImage,
} from '../features/product/productFormService'
import { convertImageToWebp } from '../shared/utils/imageConversion'
import type { SupplierProductUpdateRequest } from '../features/product/productFormTypes'

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
      mainIngredientOriginPrefectureId:
        detail.mainIngredientOriginPrefecture.id,
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

const waitForNextPaint = (): Promise<void> => {
  return new Promise((resolve) => {
    requestAnimationFrame(() => resolve())
  })
}

function ProductEditPage() {
  const { productId } = useParams<{ productId: string }>()
  const [initialValues, setInitialValues] =
    useState<ProductFormInitialValues | null>(null)
  const [error, setError] = useState('')
  const navigate = useNavigate()
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [submitProgress, setSubmitProgress] = useState('')
  const [submitError, setSubmitError] = useState('')

  useEffect(() => {
    if (!productId) {
      return
    }

    let isCancelled = false

    const loadProduct = async () => {
      try {
        const detail = await getProductDetailData(productId)

        if (!isCancelled) {
          if (!detail.permissions.canManage) {
            setError('この商品情報を編集する権限がありません。')
            return
          }
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
  }, [productId])

  const handleCancel = () => {
    navigate(-1)
  }

  if (!productId) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        商品IDが取得できませんでした。
      </p>
    )
  }

  const handleUpdate = async (values: ProductFormValues) => {
    if (isSubmitting) return

    setIsSubmitting(true)
    setSubmitError('')

    try {
      const information = values.productInformations

      const {
        name,
        productCategoryId,
        mainIngredientOriginPrefectureId,
        mainImageFile,
        contentQuantity,
        expirationType,
        shelfLifeDays,
        storageType,
        desiredRetailPrice,
      } = information
      // 必須入力に漏れがないかチェック
      if (
        !name.trim() ||
        productCategoryId === '' ||
        mainIngredientOriginPrefectureId === '' ||
        !contentQuantity.trim() ||
        expirationType === '' ||
        storageType === '' ||
        desiredRetailPrice === ''
      ) {
        throw new Error('必須項目を入力してください。')
      }

      let normalizedShelfLifeDays: number | undefined

      if (expirationType === 'NOT_APPLICABLE') {
        normalizedShelfLifeDays = undefined
      } else {
        if (shelfLifeDays === '') {
          throw new Error('賞味期限または消費期限の日数を入力してください。')
        }

        normalizedShelfLifeDays = shelfLifeDays
      }

      const preparedStories = values.stories.map((story) => {
        if (story.id === undefined) {
          throw new Error('更新対象のストーリーIDが取得できません。')
        }
        if (story.productStorySectionTemplateId === '' || !story.body.trim()) {
          throw new Error('4つのストーリーを全て入力してください。')
        }

        return {
          id: story.id,
          position: story.position,
          productStorySectionTemplateId: story.productStorySectionTemplateId,
          imageFile: story.imageFile,
          body: story.body.trim(),
        }
      })

      const monthlySupplyCapacities = values.monthlySupplyCapacities.map(
        (capacity) => {
          if (capacity.availableQuantity === '') {
            throw new Error('6ヶ月分の提供可能数量を入力してください。')
          }

          return {
            targetMonth: capacity.targetMonth,
            availableQuantity: capacity.availableQuantity,
          }
        },
      )

      const changedImageCount =
        (mainImageFile ? 1 : 0) +
        preparedStories.filter((story) => story.imageFile).length

      let uploadedImageCount = 0
      let mainImagePendingFileUploadId: string | undefined

      // メイン画像が変更された場合だけアップロード
      if (mainImageFile) {
        const current = uploadedImageCount + 1

        setSubmitProgress(
          `商品メイン画像を変換中...（${current}/${changedImageCount}）`,
        )
        await waitForNextPaint()

        const mainImageBlob = await convertImageToWebp(mainImageFile)

        setSubmitProgress(
          `商品メイン画像をアップロード中...（${current}/${changedImageCount}）`,
        )
        await waitForNextPaint()

        mainImagePendingFileUploadId = await uploadPreparedProductImage(
          mainImageBlob,
          'MAIN_IMAGE',
        )

        uploadedImageCount += 1
      }

      const productStories: SupplierProductUpdateRequest['productStories'] = []

      // 変更されたストーリー画像だけを1枚ずつアップロード
      for (const [index, story] of preparedStories.entries()) {
        let pendingFileUploadId: string | undefined

        if (story.imageFile) {
          const current = uploadedImageCount + 1

          setSubmitProgress(
            `ストーリー画像${index + 1}枚目を変換中...（${current}/${changedImageCount}）`,
          )
          await waitForNextPaint()

          const imageBlob = await convertImageToWebp(story.imageFile)

          setSubmitProgress(
            `ストーリー画像${index + 1}枚目をアップロード中...（${current}/${changedImageCount}）`,
          )
          await waitForNextPaint()

          pendingFileUploadId = await uploadPreparedProductImage(
            imageBlob,
            'STORY_IMAGE',
          )

          uploadedImageCount += 1
        }

        productStories.push({
          id: story.id,
          position: story.position,
          productStorySectionTemplateId: story.productStorySectionTemplateId,
          body: story.body,
          ...(pendingFileUploadId ? { pendingFileUploadId } : {}),
        })
      }

      const request: SupplierProductUpdateRequest = {
        name: name.trim(),
        productCategoryId,
        mainIngredientOriginPrefectureId,
        ...(mainImagePendingFileUploadId
          ? { mainImagePendingFileUploadId }
          : {}),
        contentQuantity: contentQuantity.trim(),
        expirationType,
        shelfLifeDays: normalizedShelfLifeDays,
        storageType,
        desiredRetailPrice,
        allergyInformation: information.allergyInformation?.trim() || undefined,
        certificationInformation:
          information.certificationInformation?.trim() || undefined,
        caseSize: information.caseSize?.trim() || undefined,
        unitsPerCase:
          information.unitsPerCase === ''
            ? undefined
            : information.unitsPerCase,
        minimumOrderQuantity:
          information.minimumOrderQuantity === ''
            ? undefined
            : information.minimumOrderQuantity,
        shippingLeadTimeDays:
          information.shippingLeadTimeDays === ''
            ? undefined
            : information.shippingLeadTimeDays,
        salesAreaRestriction:
          information.salesAreaRestriction?.trim() || undefined,
        monthlySupplyCapacities,
        productStories,
      }

      setSubmitProgress('商品情報を更新中...')
      await waitForNextPaint()

      await updateProduct(productId, request)

      navigate(`/products/${productId}`)
    } catch (error: unknown) {
      setSubmitError(
        error instanceof Error ? error.message : '商品の更新に失敗しました。',
      )
    } finally {
      setIsSubmitting(false)
      setSubmitProgress('')
    }
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
        onCancel={handleCancel}
        isSubmitting={isSubmitting}
        submitProgress={submitProgress}
        submitError={submitError}
      />
    </div>
  )
}

export default ProductEditPage
