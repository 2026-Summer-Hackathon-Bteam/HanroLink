import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import ProcurementRequestForm from '../features/procurementRequest/components/ProcurementRequestForm'
import { getProcurementRequestDetailData } from '../features/procurementRequest/procurementRequestDetailService'
import type { ProcurementRequestFormValues } from '../features/procurementRequest/procurementRequestFormTypes'
import type { ProcurementRequestDetailData } from '../features/procurementRequest/procurementRequestDetailTypes'
import { createTargetMonths } from '../shared/utils/yearMonth'
import { updateProcurementRequest } from '../features/procurementRequest/procurementRequestDetailService'

const convertToInitialValues = (
  detail: ProcurementRequestDetailData,
): ProcurementRequestFormValues => {
  const quantityByMonth = new Map(
    detail.monthlyProcurementQuantities.map((quantity) => [
      quantity.targetMonth,
      quantity.desiredQuantity,
    ]),
  )

  return {
    procurementRequestInformations: {
      title: detail.title,
      description: detail.description,
      productCategoryId: detail.productCategory.id,
      requiredTradeTerms: detail.requiredTradeTerms ?? '',
      desiredUnitPrice: detail.desiredUnitPrice ?? '',
      deliveryShelfLifeDays: detail.deliveryShelfLifeDays ?? '',
      storageTypes: detail.storageTypes.map((type) => type.value),
    },

    monthlyProcurementQuantities: createTargetMonths().map((targetMonth) => ({
      targetMonth,
      desiredQuantity: quantityByMonth.get(targetMonth) ?? '',
    })),
  }
}

function ProcurementRequestEditPage() {
  const { procurementRequestId } = useParams<{ procurementRequestId: string }>()
  const [initialValues, setInitialValues] =
    useState<ProcurementRequestFormValues | null>(null)
  const [error, setError] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    if (!procurementRequestId) return

    let isCancelled = false

    const loadProcurementRequest = async () => {
      try {
        const detail =
          await getProcurementRequestDetailData(procurementRequestId)

        if (!isCancelled) {
          if (!detail.permissions.canManage) {
            setError('この募集情報を編集する権限がありません。')
            return
          }
          setInitialValues(convertToInitialValues(detail))
        }
      } catch {
        if (!isCancelled) {
          setError('募集情報の取得に失敗しました。')
        }
      }
    }

    void loadProcurementRequest()

    return () => {
      isCancelled = true
    }
  }, [procurementRequestId])

  const handleCancel = () => {
    // 詳細画面に明示的に戻すと、ブラウザの戻るボタンを押した時に再度更新画面に遷移してしまうため、-1にした
    navigate(-1)
  }

  const handleUpdate = async (values: ProcurementRequestFormValues) => {
    if (!procurementRequestId) return
    const information = values.procurementRequestInformations

    if (information.productCategoryId === '') {
      throw new Error('商品カテゴリーを選択してください。')
    }

    const monthlyProcurementQuantities =
      values.monthlyProcurementQuantities.map((quantity) => {
        if (quantity.desiredQuantity === '') {
          throw new Error('希望数量を全て入力してください。')
        }
        return {
          targetMonth: quantity.targetMonth,
          desiredQuantity: quantity.desiredQuantity,
        }
      })

    await updateProcurementRequest(procurementRequestId, {
      title: information.title.trim(),
      description: information.description.trim(),
      productCategoryId: information.productCategoryId,
      requiredTradeTerms: information.requiredTradeTerms.trim() || undefined,
      desiredUnitPrice:
        information.desiredUnitPrice === ''
          ? undefined
          : information.desiredUnitPrice,
      deliveryShelfLifeDays:
        information.deliveryShelfLifeDays === ''
          ? undefined
          : information.deliveryShelfLifeDays,
      storageTypes: information.storageTypes,
      monthlyProcurementQuantities,
    })
    // 詳細画面に明示的に戻すと、ブラウザの戻るボタンを押した時に再度更新画面に遷移してしまうため、-1にした
    // -1でもProcurementRequestDetailPageが再マウントされ、useEffectが実行されるため、更新後データをAPIから取得する
    navigate(-1)
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
      <h2>募集情報編集</h2>

      <ProcurementRequestForm
        mode="edit"
        initialValues={initialValues}
        onSubmit={handleUpdate}
        onCancel={handleCancel}
      />
    </div>
  )
}

export default ProcurementRequestEditPage
