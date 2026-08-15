import type { components } from "../../shared/api/schema"

export type ProductSearchConditions = {
  targetMonths: string[]
  productCategoryIds: number[]
  storageTypes: ('AMBIENT' | 'REFRIGERATED' | 'FROZEN')[]
  mainIngredientRegionIds: number[]
}

export type ProductSearchOptions = components['schemas']['ProductSearchOptionsResponse']

type Pagination = components['schemas']['PaginationResponse']

type ProductSearchItem = Omit<components['schemas']['ProductSearchListResponse'], 'pagination'>

export type ProductSearchResult = {
    products:ProductSearchItem[]
    pagination: Pagination
}

