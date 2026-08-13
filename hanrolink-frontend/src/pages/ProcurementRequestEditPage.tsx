import { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import ProcurementRequestForm from '../features/procurementRequest/components/ProcurementRequestForm'
import { getProcurementRequestDetailData } from '../features/procurementRequest/procurementRequestDetailService'
import type { ProcurementRequestFormValues } from '../features/procurementRequest/procurementRequestFormTypes'
import type { ProcurementRequestDetailData } from '../features/procurementRequest/procurementRequestDetailTypes'
import { createTargetMonths } from '../shared/utils/yearMonth'

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
  const parsedProcurementRequestId = Number(procurementRequestId)
  const isValidProcurementRequestId =
    procurementRequestId !== undefined &&
    Number.isInteger(parsedProcurementRequestId) &&
    parsedProcurementRequestId > 0

  useEffect(() => {
    if (!isValidProcurementRequestId) return

    let isCancelled = false

    const loadProcurementRequest = async () => {
      try {
        const detail = await getProcurementRequestDetailData(
          parsedProcurementRequestId,
        )

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
  }, [isValidProcurementRequestId, parsedProcurementRequestId])

  const handleUpdate = (values: ProcurementRequestFormValues) => {
    void values
    // 募集情報更新処理を入れる
  }

  if (!isValidProcurementRequestId) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        募集情報IDが正しくありません。
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
      <h2>募集情報編集</h2>

      <ProcurementRequestForm
        mode="edit"
        initialValues={initialValues}
        onSubmit={handleUpdate}
      />
    </div>
  )
}

export default ProcurementRequestEditPage
