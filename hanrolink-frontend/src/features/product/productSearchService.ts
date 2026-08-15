import type {
  ProductSearchResult,
  ProductSearchOptions,
  ProductSearchConditions,
} from './productSearchTypes'
import {
  productSearchMock,
  productSearchOptionsMock,
} from './productSearchMock'

export function getProductSearchData(
  searchConditions: ProductSearchConditions,
  page = 1,
  pageSize = 20,
): Promise<ProductSearchResult> {
  // 実API実装時にクエリパラメーターとして使用する
  void searchConditions
  
  return Promise.resolve({
    ...productSearchMock,
    pagination: {
      ...productSearchMock.pagination,
      page,
      pageSize,
    },
  })
}

export function getProductSearchOptions(): Promise<ProductSearchOptions> {
  return Promise.resolve(productSearchOptionsMock)
}
