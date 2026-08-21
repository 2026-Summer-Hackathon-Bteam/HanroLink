import { authenticatedApi } from '../../lib/api'
import type { AdminBusinessApprovalDetail } from './adminApprovalTypes'

export async function getAdminBusinessApprovalDetail(businessId:string): Promise<AdminBusinessApprovalDetail> {
    const {data, response} = await authenticatedApi.GET('/api/v1/admin/business-registrations/{businessId}', {
      params: {
        path: {
          businessId,
        }
      }
    })

    if(!response.ok || !data) {
      throw new Error(`新規登録者詳細情報の取得に失敗しました。（ステータス：${response.status}）`)
    }
  return data
}

export async function approveBusiness(businessId:string): Promise<void> {
  const {response} = await authenticatedApi.PATCH('/api/v1/admin/business-registrations/{businessId}/approve', {
    params: {
      path: {
        businessId,
      }
    }
  })
  if(!response.ok || response.status !== 204) {
    throw new Error(`新規登録者の承認に失敗しました。（ステータス：${response.status}）`)
  }
}
