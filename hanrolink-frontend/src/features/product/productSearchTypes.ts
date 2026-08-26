import type { components } from '../../shared/api/schema'

type StorageType = components['schemas']['StorageTypeOptionResponse']['value']

export type ProductSearchConditions = {
  targetMonths: string[]
  productCategoryIds: number[]
  storageTypes: StorageType[]
  mainIngredientRegionIds: number[]
}

export type ProductSearchOptions =
  components['schemas']['ProductSearchOptionsResponse']

export type ProductSearchResult = components['schemas']['ProductSearchResponse']
