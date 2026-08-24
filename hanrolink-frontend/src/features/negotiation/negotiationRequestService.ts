import { authenticatedApi } from '../../lib/api'

export async function createProductNegotiationRequest(
  productId: string,
): Promise<void> {
  const { response } = await authenticatedApi.POST(
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
    throw new Error('商談希望の送信に失敗しました。')
  }
}
