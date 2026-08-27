import type {
  ProcurementRequestSearchConditions,
  ProcurementRequestSearchOptions,
  ProcurementRequestSearchResult,
} from './procurementRequestSearchTypes'
import { authenticatedApi } from '../../lib/api'

export async function getProcurementRequestSearchData(
  searchConditions: ProcurementRequestSearchConditions,
  page = 1,
  pageSize = 20,
): Promise<ProcurementRequestSearchResult> {
  const keyword = searchConditions.keyword.trim()

  const { data, response } = await authenticatedApi.GET(
    '/api/v1/procurement-requests',
    {
      params: {
        query: {
          desiredProcurementMonths:
            searchConditions.desiredProcurementMonths.length > 0
              ? searchConditions.desiredProcurementMonths
              : undefined,
          productCategoryIds:
            searchConditions.productCategoryIds.length > 0
              ? searchConditions.productCategoryIds
              : undefined,
          storageTypes:
            searchConditions.storageTypes.length > 0
              ? searchConditions.storageTypes
              : undefined,
          keyword: keyword.length > 0 ? keyword : undefined,
          page,
          pageSize,
        },
      },
    },
  )

  if (!response.ok || !data) {
    throw new Error('募集情報一覧の取得に失敗しました。')
  }

  return data
}

export async function getProcurementRequestSearchOptions(): Promise<ProcurementRequestSearchOptions> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/procurement-requests/search-options',
  )

  if (!response.ok || !data) {
    throw new Error('募集情報検索条件の取得に失敗しました。')
  }

  return data
}