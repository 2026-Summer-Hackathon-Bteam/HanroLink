import type { components } from '../../shared/api/schema'

export type CurrentBusiness =
  components['schemas']['CurrentBusinessGetResponse']

export type BuyerReceivedNegotiation =
  components['schemas']['BuyerProcurementNegotiationRequestListResponse']

export type BuyerSentNegotiation =
  components['schemas']['BuyerProductNegotiationRequestListResponse']

export type BuyerProcurementRequest =
  components['schemas']['BuyerProcurementRequestListResponse']

export type MyChat = components['schemas']['MyChatListResponse']

export type BuyerMyPageData = {
  business: CurrentBusiness
  receivedNegotiations: BuyerReceivedNegotiation[]
  sentNegotiations: BuyerSentNegotiation[]
  procurementRequests: BuyerProcurementRequest[]
  chats: MyChat[]
}

export type BuyerAcceptNegotiationResponse = components['schemas']['NegotiationRequestAcceptResponse']
