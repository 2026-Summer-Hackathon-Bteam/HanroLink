import type { components } from '../../shared/api/schema'

type ProcurementRequestCreateRequest =
  components['schemas']['BuyerProcurementRequestCreateRequest']

export type ProcurementRequestFormData = Omit<
  ProcurementRequestCreateRequest,
  | 'productCategoryId'
  | 'requiredTradeTerms'
  | 'desiredUnitPrice'
  | 'deliveryShelfLifeDays'
  | 'monthlyProcurementQuantities'
> & {
  productCategoryId: number | ''
  requiredTradeTerms: string
  desiredUnitPrice: number | ''
  deliveryShelfLifeDays: number | ''
}

type MonthlyProcurementQuantityRequest =
  components['schemas']['MonthlyProcurementQuantityRequest']

export type MonthlyProcurementQuantityFormData = Omit<
  MonthlyProcurementQuantityRequest,
  'desiredQuantity'
> & {
  desiredQuantity: number | ''
}

export type ProcurementRequestFormOptions =
  components['schemas']['BuyerProcurementRequestFormOptionsResponse']

export type ProcurementRequestFormValues = {
  procurementRequestInformations:ProcurementRequestFormData
  monthlyProcurementQuantities:MonthlyProcurementQuantityFormData[]
}

export type ProcurementRequestFormMode = 'create' | 'edit'

export type ProcurementRequestFormProps = {
  mode:ProcurementRequestFormMode
  initialValues:ProcurementRequestFormValues
  onSubmit: (values: ProcurementRequestFormValues) => void | Promise<void>
}