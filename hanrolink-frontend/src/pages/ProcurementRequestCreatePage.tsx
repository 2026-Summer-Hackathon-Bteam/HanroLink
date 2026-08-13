import { useState } from 'react'
import ProcurementRequestForm from '../features/procurementRequest/components/ProcurementRequestForm'
import type { ProcurementRequestFormValues } from '../features/procurementRequest/procurementRequestFormTypes'
import { createTargetMonths } from '../shared/utils/yearMonth'

const createProcurementRequestInitialValues = ():ProcurementRequestFormValues => {
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
  const [initialValues] = useState<ProcurementRequestFormValues>(createProcurementRequestInitialValues)

  const handleCreate = (values: ProcurementRequestFormValues) => {
    void values
    // 送信処理を書く
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
