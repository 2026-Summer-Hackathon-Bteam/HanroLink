
import type {
  CurrentBusiness,
  MyChat,
  ReceivedNegotiation,
  SentNegotiation,
  SupplierMyPageData,
  SupplierProduct,
  AcceptNegotiationResponse,
} from './supplierMyPageTypes'
import { authenticatedApi } from '../../lib/api'

export async function getCurrentBusiness(): Promise<CurrentBusiness> {
  const { data, response } = await authenticatedApi.GET('/api/v1/me/business')

  if (!response.ok || !data) {
    throw new Error('事業者名の取得に失敗しました。')
  }
  return data
}

export async function getReceivedNegotiations(): Promise<
  ReceivedNegotiation[]
> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/me/supplier/product-negotiation-requests',
  )

  if (!response.ok || !data) {
    throw new Error('商談希望一覧（受信）の取得に失敗しました。')
  }
  return data
}

export async function getSentNegotiations(): Promise<SentNegotiation[]> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/me/supplier/procurement-negotiation-requests',
  )

  if (!response.ok || !data) {
    throw new Error('商談希望一覧（送信）の取得に失敗しました。')
  }
  return data
}

export async function getSupplierProducts(): Promise<SupplierProduct[]> {
  const { data, response } = await authenticatedApi.GET('/api/v1/me/products')

  if (!response.ok || !data) {
    throw new Error('自社商品一覧の取得に失敗しました。')
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

export async function getSupplierMyPageData(): Promise<SupplierMyPageData> {
  const [business, receivedNegotiations, sentNegotiations, products, chats] =
    await Promise.all([
      getCurrentBusiness(),
      getReceivedNegotiations(),
      getSentNegotiations(),
      getSupplierProducts(),
      getMyChats(),
    ])

  return {
    business,
    receivedNegotiations,
    sentNegotiations,
    products,
    chats,
  }
}

export async function acceptNegotiationRequest(
  productNegotiationRequestId: number,
): Promise<AcceptNegotiationResponse> {
  const { data, response } = await authenticatedApi.POST(
    '/api/v1/me/supplier/product-negotiation-requests/{productNegotiationRequestId}/accept', {
      params: {
        path: {
          productNegotiationRequestId,
        }
      }
    }
  )

  if(!response.ok || !data) {
    throw new Error('商談の開始に失敗しました。')
  }

  return data
}
