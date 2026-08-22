import { useState } from 'react'
import ProductForm from '../features/product/components/ProductForm'
import type { ProductFormValues } from '../features/product/productFormTypes'
import { createTargetMonths } from '../shared/utils/yearMonth'

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

function ProductCreatePage() {
  const [initialValues] = useState<ProductFormValues>(
    createProductInitialValues,
  )

  const handleCreate = (values: ProductFormValues) => {
    void values
    // 送信処理を書く
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
      />
    </div>
  )
}

export default ProductCreatePage
