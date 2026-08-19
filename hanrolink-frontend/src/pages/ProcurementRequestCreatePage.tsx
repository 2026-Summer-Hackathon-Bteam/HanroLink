import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import ProcurementRequestForm from '../features/procurementRequest/components/ProcurementRequestForm'
import type { ProcurementRequestFormValues } from '../features/procurementRequest/procurementRequestFormTypes'
import { createTargetMonths } from '../shared/utils/yearMonth'
import { createProcurementRequest } from '../features/procurementRequest/procurementRequestFormService'

const createProcurementRequestInitialValues =
  (): ProcurementRequestFormValues => {
    return {
      procurementRequestInformations: {
        title: '',
        description: '',
        productCategoryId: '',
        requiredTradeTerms: '',
        desiredUnitPrice: '',
        deliveryShelfLifeDays: '',
        storageTypes: [],
      },
      monthlyProcurementQuantities: createTargetMonths().map((targetMonth) => ({
        targetMonth,
        desiredQuantity: '',
      })),
    }
  }

function ProcurementRequestCreatePage() {
  const [initialValues] = useState<ProcurementRequestFormValues>(
    createProcurementRequestInitialValues,
  )
  const navigate = useNavigate()

  const handleCreate = async (values: ProcurementRequestFormValues) => {
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

    const result = await createProcurementRequest({
      title: information.title.trim(),
      description: information.description.trim(),
      productCategoryId:information.productCategoryId,
      requiredTradeTerms: information.requiredTradeTerms.trim() || undefined,
      desiredUnitPrice: information.desiredUnitPrice === '' ? undefined : information.desiredUnitPrice,
      deliveryShelfLifeDays: information.deliveryShelfLifeDays === '' ? undefined : information.deliveryShelfLifeDays,
      storageTypes: information.storageTypes,
      monthlyProcurementQuantities,
    })

    navigate(`/procurement-requests/${result.id}`, {
      replace: true
    })
  }

  return (
    <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
      <h2>募集情報登録</h2>
      <p className="mb-8">
        HanroLinkはサプライヤーの“今なら提供できる商品”と、バイヤーの“この時期に仕入れたい商品”をつなぐサービスです。
        <br />
        募集情報には希望する商品の説明だけでなく、「いつ・どれくらい」希望しているかを入力してください。
        <br />
        サプライヤーの供給可能時期と、バイヤーの募集時期が合うことで、より商談につながりやすくなります。
      </p>

      <ProcurementRequestForm
        mode="create"
        initialValues={initialValues}
        onSubmit={handleCreate}
      />
    </div>
  )
}

export default ProcurementRequestCreatePage
