import { authenticatedApi } from '../../lib/api'
import type { BuyerProfile } from './buyerProfileTypes'

export async function getBuyerProfile(
  businessId: string,
): Promise<BuyerProfile> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/buyer/{businessId}',
    {
      params: {
        path: {
          businessId,
        },
      },
    },
  )

  if (!response.ok || !data) {
    throw new Error('バイヤー情報の取得に失敗しました。')
  }

  return data
}
