import type { PendingBusinessRegistration } from './adminMyPageTypes'
import { authenticatedApi } from '../../lib/api'

export async function getPendingBusinessRegistrations(): Promise<
  PendingBusinessRegistration[]
> {
  const {data, response} = await authenticatedApi.GET('/api/v1/admin/business-registrations/pending')

  if(!response.ok || !data) {
    throw new Error(`新規登録者一覧の取得に失敗しました。（ステータス：${response.status}）`)
  }
  return data
}
