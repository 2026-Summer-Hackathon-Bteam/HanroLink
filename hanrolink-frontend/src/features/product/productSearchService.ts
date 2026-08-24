import type {
  ProductSearchResult,
  ProductSearchOptions,
  ProductSearchConditions,
} from './productSearchTypes'
import { authenticatedApi } from '../../lib/api'

export async function getProductSearchData(
  searchConditions: ProductSearchConditions,
  page = 1,
  pageSize = 20,
): Promise<ProductSearchResult> {
  const { data, response } = await authenticatedApi.GET('/api/v1/products', {
    params: {
      query: {
        availableSupplyMonths:
          searchConditions.targetMonths.length > 0
            ? searchConditions.targetMonths
            : undefined,
        mainIngredientOriginRegionIds:
          searchConditions.mainIngredientRegionIds.length > 0
            ? searchConditions.mainIngredientRegionIds
            : undefined,
        productCategoryIds:
          searchConditions.productCategoryIds.length > 0
            ? searchConditions.productCategoryIds
            : undefined,
        storageTypes:
          searchConditions.storageTypes.length > 0
            ? searchConditions.storageTypes
            : undefined,
        page,
        pageSize,
      },
    },
  })

  if (!response.ok || !data) {
    throw new Error('商品一覧の取得に失敗しました。')
  }

  return data
}

export async function getProductSearchOptions(): Promise<ProductSearchOptions> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/products/search-options',
  )

  if (!response.ok || !data) {
    throw new Error('商品検索条件の取得に失敗しました。')
  }

  return data
}
