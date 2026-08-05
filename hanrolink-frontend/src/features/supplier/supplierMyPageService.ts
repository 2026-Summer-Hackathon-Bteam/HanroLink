import { supplierMyPageMock } from './supplierMyPageMock'
import type {
  CurrentBusiness,
  MyChat,
  ReceivedNegotiation,
  SentNegotiation,
  SupplierMyPageData,
  SupplierProduct,
} from './supplierMyPageTypes'

export function getCurrentBusiness(): Promise<CurrentBusiness> {
  return Promise.resolve(supplierMyPageMock.business)
}

export function getReceivedNegotiations(): Promise<
  ReceivedNegotiation[]
> {
  return Promise.resolve(supplierMyPageMock.receivedNegotiations)
}

export function getSentNegotiations(): Promise<SentNegotiation[]> {
  return Promise.resolve(supplierMyPageMock.sentNegotiations)
}

export function getSupplierProducts(): Promise<SupplierProduct[]> {
  return Promise.resolve(supplierMyPageMock.products)
}

export function getMyChats(): Promise<MyChat[]> {
  return Promise.resolve(supplierMyPageMock.chats)
}

export async function getSupplierMyPageData(): Promise<SupplierMyPageData> {
  const [
    business,
    receivedNegotiations,
    sentNegotiations,
    products,
    chats,
  ] = await Promise.all([
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