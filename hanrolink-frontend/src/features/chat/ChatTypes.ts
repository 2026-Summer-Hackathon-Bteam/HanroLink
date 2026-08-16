import type { components } from '../../shared/api/schema'

export type ChatDetail =
  components['schemas']['MyChatDetailResponse']

export type ChatMessage =
  components['schemas']['MyChatMessageListResponse']

export type ChatMessages = ChatMessage[]