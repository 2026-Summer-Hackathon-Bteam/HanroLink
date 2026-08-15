import type { components } from '../../shared/api/schema'

type ProcurementRequestSearchItem = Omit<
  components['schemas']['ProcurementRequestListResponse'],
  'pagination'>

type Pagination = components['schemas']['PaginationResponse']

export type ProcurementRequestSearchResult = {
  procurementRequests: ProcurementRequestSearchItem[]
  pagination: Pagination
}

export type ProcurementRequestSearchOptions =
  components['schemas']['ProcurementRequestSearchOptionsResponse']

export type ProcurementRequestSearchConditions = {
  keyword: string
  productCategoryIds: number[]
  storageTypes: Array<
    ProcurementRequestSearchOptions['storageTypes'][number]['value']
  >
}