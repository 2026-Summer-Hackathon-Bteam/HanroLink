import type { ProcurementRequestFormOptions } from './procurementRequestFormTypes'
import { authenticatedApi } from '../../lib/api'
import type { components } from '../../shared/api/schema'

type ProcurementRequestCreateSubmission =
  components['schemas']['BuyerProcurementRequestCreateRequest']

type ProcurementRequestCreateResponse =
  components['schemas']['BuyerProcurementRequestCreateResponse']

export async function getProcurementRequestFormOptions(): Promise<ProcurementRequestFormOptions> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/procurement-requests/form-options',
  )

  if (!data || !response.ok) {
    throw new Error(
      `募集情報フォーム選択肢の取得に失敗しました。（ステータス：${response.status}）`,
    )
  }
  return data
}

export async function createProcurementRequest(
  request: ProcurementRequestCreateSubmission,
): Promise<ProcurementRequestCreateResponse> {
  const { data, response } = await authenticatedApi.POST(
    '/api/v1/procurement-requests',
    {
      body: request,
    },
  )

  if (!data || !response.ok) {
    throw new Error(
      `募集情報の登録に失敗しました。（ステータス：${response.status}）`,
    )
  }
  return data
}
