import type { ProcurementRequestDetailData } from './procurementRequestDetailTypes'
import { authenticatedApi } from '../../lib/api'

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
