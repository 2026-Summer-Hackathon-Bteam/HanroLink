import type { components } from '../../shared/api/schema'

export type ChatDetail =
  components['schemas']['MyChatDetailResponse']

export type ChatMessage =
  components['schemas']['MyChatMessageListResponse']

export type ChatMessages = ChatMessage[]

export type ChatMessageCreateRequest =
  components['schemas']['MyChatMessageCreateRequest']

export type ChatFileUploadCreateRequest =
  components['schemas']['MyChatFileUploadCreateRequest']

export type ChatFileUploadCreateResponse =
  components['schemas']['MyChatFileUploadCreateResponse']

export type ChatFileMimeType =
  ChatFileUploadCreateRequest['mimeType']