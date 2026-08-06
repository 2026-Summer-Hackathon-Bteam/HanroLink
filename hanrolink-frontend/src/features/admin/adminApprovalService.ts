import type { AdminBusinessApprovalDetail } from './adminApprovalTypes'
import { adminApprovalMock } from './adminApprovalMock'

export function getAdminBusinessApprovalDetail(businessUserAccountId:string): Promise<AdminBusinessApprovalDetail> {
    if(adminApprovalMock.businessUserAccount.id !== businessUserAccountId){
        return Promise.reject(new Error('対象の新規登録者が見つかりません。'))
    }
  return Promise.resolve(adminApprovalMock)
}
