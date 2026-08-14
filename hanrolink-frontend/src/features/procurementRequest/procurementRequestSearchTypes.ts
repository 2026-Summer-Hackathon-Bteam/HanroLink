import type { components } from "../../shared/api/schema";

type ProcurementRequestSearchItem = components['schemas']['ProcurementRequestListResponse']

type Pagination = components['schemas']['PaginationResponse']

export type ProcurementRequestSearchResult = {
  procurementRequests: ProcurementRequestSearchItem[]
  pagination: Pagination
}