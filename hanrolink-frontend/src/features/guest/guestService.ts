import type { GuestData } from './guestTypes'
import { api } from '../../lib/api'

export async function getGuestData(): Promise<GuestData> {
  const { data, response } = await api.GET('/api/v1/public/products')

  if (!response.ok || !data) {
    throw new Error(
      `商品情報の取得に失敗しました。（ステータス：${response.status}）`,
    )
  }

  return data
}
