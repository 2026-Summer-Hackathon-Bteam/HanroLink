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
