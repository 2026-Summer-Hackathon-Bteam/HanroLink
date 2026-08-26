import type {
  CurrentBusiness,
  BuyerReceivedNegotiation,
  BuyerSentNegotiation,
  BuyerProcurementRequest,
  MyChat,
  BuyerMyPageData,
  BuyerAcceptNegotiationResponse,
} from './buyerMyPageTypes'
import { authenticatedApi } from '../../lib/api'

export async function getCurrentBusiness(): Promise<CurrentBusiness> {
  const { data, response } = await authenticatedApi.GET('/api/v1/me/business')

  if (!response.ok || !data) {
    throw new Error('事業者名の取得に失敗しました。')
  }
  return data
}

export async function getReceivedNegotiations(): Promise<
  BuyerReceivedNegotiation[]
> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/me/buyer/procurement-negotiation-requests',
  )

  if (!response.ok || !data) {
    throw new Error('商談希望一覧（受信）の取得に失敗しました。')
  }
  return data
}

export async function getSentNegotiations(): Promise<BuyerSentNegotiation[]> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/me/buyer/product-negotiation-requests',
  )

  if (!response.ok || !data) {
    throw new Error('商談希望一覧（送信）取得に失敗しました。')
  }
  return data
}

export async function getBuyerProcurementRequests(): Promise<
  BuyerProcurementRequest[]
> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/me/procurement-requests',
  )

  if (!response.ok || !data) {
    throw new Error('自社募集情報一覧の取得に失敗しました。')
  }
  return data
}

export async function getMyChats(): Promise<MyChat[]> {
  const { data, response } = await authenticatedApi.GET('/api/v1/me/chats')

  if (!response.ok || !data) {
    throw new Error('チャット一覧の取得に失敗しました。')
  }
  return data
}

export async function getBuyerMyPageData(): Promise<BuyerMyPageData> {
  const [
    business,
    receivedNegotiations,
    sentNegotiations,
    procurementRequests,
    chats,
  ] = await Promise.all([
    getCurrentBusiness(),
    getReceivedNegotiations(),
    getSentNegotiations(),
    getBuyerProcurementRequests(),
    getMyChats(),
  ])
  return {
    business,
    receivedNegotiations,
    sentNegotiations,
    procurementRequests,
    chats,
  }
}

export async function acceptNegotiationRequest(
  procurementNegotiationRequestId: number,
): Promise<BuyerAcceptNegotiationResponse> {
  const { data, response } = await authenticatedApi.POST(
    '/api/v1/me/buyer/procurement-negotiation-requests/{procurementNegotiationRequestId}/accept',
    {
      params: {
        path: {
          procurementNegotiationRequestId,
        },
      },
    },
  )

  if (!response.ok || !data) {
    throw new Error('商談の開始に失敗しました。')
  }
  return data
}
