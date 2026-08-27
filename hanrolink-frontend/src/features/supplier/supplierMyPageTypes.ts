import type { components } from '../../shared/api/schema'

export type CurrentBusiness =
  components['schemas']['CurrentBusinessGetResponse']

export type ReceivedNegotiation =
  components['schemas']['SupplierReceivedNegotiationRequestListResponse']

export type SentNegotiation =
  components['schemas']['SupplierSentNegotiationRequestListResponse']

export type SupplierProduct =
  components['schemas']['SupplierProductListResponse']

export type MyChat = components['schemas']['MyChatListResponse']

export type SupplierMyPageData = {
  business: CurrentBusiness
  receivedNegotiations: ReceivedNegotiation[]
  sentNegotiations: SentNegotiation[]
  products: SupplierProduct[]
  chats: MyChat[]
}

export type AcceptNegotiationResponse = components['schemas']['NegotiationRequestAcceptResponse']
