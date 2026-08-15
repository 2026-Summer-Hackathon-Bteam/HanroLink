import type {
  ProcurementRequestSearchConditions,
  ProcurementRequestSearchOptions,
  ProcurementRequestSearchResult,
} from './procurementRequestSearchTypes'
import {
  procurementRequestSearchMock,
  procurementRequestSearchOptionsMock,
} from './procurementRequestSearchMock'

export function getProcurementRequestSearchData(
  searchConditions: ProcurementRequestSearchConditions,
  page = 1,
  pageSize = 20,
): Promise<ProcurementRequestSearchResult> {
  // 実API実装時にクエリパラメーターとして使用する
  void searchConditions

  return Promise.resolve({
    ...procurementRequestSearchMock,
    pagination: {
      ...procurementRequestSearchMock.pagination,
      page,
      pageSize,
    },
  })
}

export function getProcurementRequestSearchOptions():
  Promise<ProcurementRequestSearchOptions> {
  return Promise.resolve(procurementRequestSearchOptionsMock)
}