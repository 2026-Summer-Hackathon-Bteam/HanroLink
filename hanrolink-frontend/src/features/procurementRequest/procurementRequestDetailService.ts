import type { ProcurementRequestDetailData } from "./procurementRequestDetailTypes"
import { procurementRequestDetailMock } from "./procurementRequestDetailMock"

export function getProcurementRequestDetailData(
  procurementRequestId: number,
): Promise<ProcurementRequestDetailData> {
  if (procurementRequestId !== 1) {
    return Promise.reject(new Error('対象の募集情報が見つかりません。'))
  }

  return Promise.resolve(procurementRequestDetailMock)
}

export function deleteProcurementRequest(procurementRequestId: number, ): Promise<void> {
   // バックエンドとの通信時はIdをパスに入れる
    void procurementRequestId

    return Promise.resolve()
}