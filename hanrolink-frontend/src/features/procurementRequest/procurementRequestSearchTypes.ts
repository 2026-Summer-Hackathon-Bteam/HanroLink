import type { components } from '../../shared/api/schema'

type StorageType =
  components['schemas']['StorageTypeOptionResponse']['value']

export type ProcurementRequestSearchConditions = {
  keyword: string
  desiredProcurementMonths: string[]
  productCategoryIds: number[]
  storageTypes: StorageType[]
}

export type ProcurementRequestSearchOptions =
  components['schemas']['ProcurementRequestSearchOptionsResponse']

export type ProcurementRequestSearchResult =
  components['schemas']['ProcurementRequestSearchResponse']