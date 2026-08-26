import { useState } from 'react'
import ProductForm from '../features/product/components/ProductForm'
import type {
  ProductFormValues,
  SupplierProductCreateRequest,
} from '../features/product/productFormTypes'
import { createTargetMonths } from '../shared/utils/yearMonth'
import { useNavigate } from 'react-router-dom'
import {
  createProduct,
  uploadPreparedProductImage,
} from '../features/product/productFormService'
import { convertImageToWebp } from '../shared/utils/imageConversion'

const createProductInitialValues = (): ProductFormValues => {
  return {
    productInformations: {
      name: '',
      productCategoryId: '',
      mainIngredientOriginPrefectureId: '',
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
    },
    stories: [
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
    ],
    monthlySupplyCapacities: createTargetMonths().map((targetMonth) => ({
      targetMonth,
      availableQuantity: '',
    })),
  }
}

const waitForNextPaint = (): Promise<void> => {
  return new Promise((resolve) => {
    requestAnimationFrame(() => resolve())
  })
}

function ProductCreatePage() {
  const [initialValues] = useState<ProductFormValues>(
    createProductInitialValues,
  )
  const navigate = useNavigate()
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [submitProgress, setSubmitProgress] = useState('')
  const [submitError, setSubmitError] = useState('')

  const handleCreate = async (values: ProductFormValues) => {
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
        !mainImageFile ||
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
        if (
          story.productStorySectionTemplateId === '' ||
          !story.imageFile ||
          !story.body.trim()
        ) {
          throw new Error('4つのストーリーを全て入力してください。')
        }

        return {
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
      // ブラウザの負荷が高くならないように１枚ずつ変換・アップロード
      setSubmitProgress('商品メイン画像を変換中...（1/5）')
      await waitForNextPaint()

      const mainImageBlob = await convertImageToWebp(mainImageFile)

      setSubmitProgress('商品メイン画像をアップロード中...（1/5）')
      await waitForNextPaint()

      const mainImagePendingFileUploadId = await uploadPreparedProductImage(
        mainImageBlob,
        'MAIN_IMAGE',
      )

      const productStories: SupplierProductCreateRequest['productStories'] = []

      for (const [index, story] of preparedStories.entries()) {
        const current = index + 2

        setSubmitProgress(
          `ストーリー画像${index + 1}枚目を変換中...（${current}/5）`,
        )
        await waitForNextPaint()

        const blob = await convertImageToWebp(story.imageFile)

        setSubmitProgress(
          `ストーリー画像${index + 1}枚目をアップロード中...（${current}/5）`,
        )
        await waitForNextPaint()

        const pendingFileUploadId = await uploadPreparedProductImage(
          blob,
          'STORY_IMAGE',
        )

        productStories.push({
          position: story.position,
          productStorySectionTemplateId: story.productStorySectionTemplateId,
          body: story.body,
          pendingFileUploadId,
        })
      }

      const request: SupplierProductCreateRequest = {
        name: name.trim(),
        productCategoryId,
        mainIngredientOriginPrefectureId,
        mainImagePendingFileUploadId,
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

      setSubmitProgress('商品情報を登録中...')
      await waitForNextPaint()

      const result = await createProduct(request)

      navigate(`/products/${result.id}`)
    } catch (error: unknown) {
      setSubmitError(
        error instanceof Error ? error.message : '商品の登録に失敗しました。',
      )
    } finally {
      setIsSubmitting(false)
      setSubmitProgress('')
    }
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
      <ProductForm
        mode="create"
        initialValues={initialValues}
        onSubmit={handleCreate}
        isSubmitting={isSubmitting}
        submitProgress={submitProgress}
        submitError={submitError}
      />
    </div>
  )
}

export default ProductCreatePage
