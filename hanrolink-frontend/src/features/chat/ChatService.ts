import type {
  ChatDetail,
  ChatMessages,
  ChatMessageCreateRequest,
  ChatFileMimeType,
  ChatFileUploadCreateRequest,
  ChatFileUploadCreateResponse,
} from './ChatTypes'
import { authenticatedApi } from '../../lib/api'

export async function getChatMessages(
  channelId: string,
): Promise<ChatMessages> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/chats/{channelId}/messages',
    {
      params: {
        path: {
          channelId,
        },
      },
    },
  )

  if (!response.ok || !data) {
    throw new Error('メッセージ一覧の取得に失敗しました。')
  }

  return data
}

export async function getChatDetail(channelId: string): Promise<ChatDetail> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/chats/{channelId}',
    {
      params: {
        path: {
          channelId,
        },
      },
    },
  )

  if (!response.ok || !data) {
    throw new Error('チャット情報の取得に失敗しました。')
  }

  return data
}

export async function createChatMessage(
  channelId: string,
  request: ChatMessageCreateRequest,
): Promise<void> {
  const { response } = await authenticatedApi.POST(
    '/api/v1/chats/{channelId}/messages',
    {
      params: {
        path: {
          channelId,
        },
      },
      body: request,
    },
  )

  if (!response.ok || response.status !== 201) {
    throw new Error('メッセージの送信に失敗しました。')
  }
}

export async function createChatFileUploadInformation(
  request: ChatFileUploadCreateRequest,
): Promise<ChatFileUploadCreateResponse> {
  const { data, response } = await authenticatedApi.POST(
    '/api/v1/chats/file-uploads',
    {
      body: request,
    },
  )

  if (!response.ok || response.status !== 201 || !data) {
    throw new Error('ファイルアップロードURLの取得に失敗しました。')
  }

  return data
}

export async function uploadChatFile(
  uploadUrl: string,
  file: Blob,
  mimeType: ChatFileMimeType,
): Promise<void> {
  const response = await fetch(uploadUrl, {
    method: 'PUT',
    headers: {
      'Content-Type': mimeType,
    },
    body: file,
  })

  if (!response.ok) {
    throw new Error('ファイルのアップロードに失敗しました。')
  }
}

export async function uploadPreparedChatFile(
  file: Blob,
  displayFilename: string,
  mimeType: ChatFileMimeType,
): Promise<string> {
  if (file.type !== mimeType) {
    throw new Error(
      'アップロードするファイルの形式を確認できませんでした。',
    )
  }

  const { uploadUrl, pendingFileUploadId } =
    await createChatFileUploadInformation({
      mimeType,
      displayFilename,
      fileSizeBytes: file.size,
    })

  await uploadChatFile(uploadUrl, file, mimeType)

  return pendingFileUploadId
}