import { authenticatedApi } from '../../lib/api'
import type { NegotiationSelectableProduct } from './negotiationRequestTypes'
import { getApiErrorMessage } from '../../shared/api/apiError'

const negotiationRequestFieldLabels: Record<string, string> = {
  productId: '商品',
}

export async function createProductNegotiationRequest(
  productId: string,
): Promise<void> {
  const { error, response } = await authenticatedApi.POST(
    '/api/v1/products/{productId}/negotiation-requests',
    {
      params: {
        path: {
          productId,
        },
      },
    },
  )

  if (!response.ok || response.status !== 201) {
    throw new Error(
      getApiErrorMessage(
        error,
        '商談希望の送信に失敗しました。',
        negotiationRequestFieldLabels,
      ),
    )
  }
}

export async function getNegotiationSelectableProducts(): Promise<
  NegotiationSelectableProduct[]
> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/procurement-negotiation-requests/selectable-products',
  )

  if (!response.ok || !data) {
    throw new Error('選択可能な商品の取得に失敗しました。')
  }

  return data
}

export async function createProcurementNegotiationRequest(
  procurementRequestId: string,
  productId: string,
): Promise<void> {
  const { error, response } = await authenticatedApi.POST(
    '/api/v1/procurement-requests/{procurementRequestId}/negotiation-requests',
    {
      params: {
        path: {
          procurementRequestId,
        },
      },
      body: {
        productId,
      },
    },
  )

  if (!response.ok || response.status !== 201) {
    throw new Error(
      getApiErrorMessage(
        error,
        '商談希望の送信に失敗しました。',
        negotiationRequestFieldLabels,
      ),
    )
  }
}
