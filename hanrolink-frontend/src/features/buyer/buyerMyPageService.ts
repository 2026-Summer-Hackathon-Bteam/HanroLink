import { buyerMyPageMock } from './buyerMyPageMock'
import type {
  CurrentBusiness,
  BuyerReceivedNegotiation,
  BuyerSentNegotiation,
  BuyerProcurementRequest,
  MyChat,
  BuyerMyPageData,
} from './buyerMyPageTypes'

export function getCurrentBusiness(): Promise<CurrentBusiness> {
  return Promise.resolve(buyerMyPageMock.business)
}

export function getReceivedNegotiations(): Promise<BuyerReceivedNegotiation[]> {
  return Promise.resolve(buyerMyPageMock.receivedNegotiations)
}

export function getSentNegotiations(): Promise<BuyerSentNegotiation[]> {
  return Promise.resolve(buyerMyPageMock.sentNegotiations)
}

export function getBuyerProcurementRequests(): Promise<
  BuyerProcurementRequest[]
> {
  return Promise.resolve(buyerMyPageMock.procurementRequests)
}

export function getMyChats(): Promise<MyChat[]> {
  return Promise.resolve(buyerMyPageMock.chats)
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
