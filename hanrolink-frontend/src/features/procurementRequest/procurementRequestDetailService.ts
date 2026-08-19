import type { ProcurementRequestDetailData } from './procurementRequestDetailTypes'
import { authenticatedApi } from '../../lib/api'
import type { components } from '../../shared/api/schema'

type ProcurementRequestUpdateSubmission =
  components['schemas']['BuyerProcurementRequestUpdateRequest']

export async function getProcurementRequestDetailData(
  procurementRequestId: string,
): Promise<ProcurementRequestDetailData> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/procurement-requests/{procurementRequestId}',
    {
      params: {
        path: {
          procurementRequestId,
        },
      },
    },
  )

  if (!response.ok || !data) {
    throw new Error(
      `募集情報詳細の取得に失敗しました。（ステータス：${response.status}）`,
    )
  }

  return data
}

export async function deleteProcurementRequest(
  procurementRequestId: string,
): Promise<void> {
  const { response } = await authenticatedApi.DELETE(
    '/api/v1/procurement-requests/{procurementRequestId}',
    {
      params: {
        path: {
          procurementRequestId,
        },
      },
    },
  )

  if (!response.ok || response.status !== 204) {
    throw new Error(
      `募集情報の削除に失敗しました。（ステータス：${response.status}）`,
    )
  }
}

export async function updateProcurementRequest(
  procurementRequestId: string,
  request: ProcurementRequestUpdateSubmission,
): Promise<void> {
  const { response } = await authenticatedApi.PUT(
    '/api/v1/procurement-requests/{procurementRequestId}',
    {
      params: {
        path: {
          procurementRequestId,
        },
      },
      body: request,
    },
  )

  if (!response.ok || response.status !== 204) {
    throw new Error(
      `募集情報の更新に失敗しました。（ステータス：${response.status}）`,
    )
  }
}
