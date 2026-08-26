import {
  useState,
  type ChangeEvent,
  useRef,
  type SubmitEvent,
  useEffect,
  useLayoutEffect,
} from 'react'
import { useParams } from 'react-router-dom'
import type {
  ChatDetail,
  ChatMessages,
  ChatNegotiationSnapshot,
} from '../features/chat/ChatTypes'
import {
  getChatDetail,
  getChatMessages,
  createChatMessage,
  uploadPreparedChatFile,
  getChatNegotiationSnapshot,
} from '../features/chat/ChatService'
import { convertImageToWebp } from '../shared/utils/imageConversion'
import NegotiationSnapshotComparison from '../features/chat/components/NegotiationSnapshotComparison'

const formatChatDateTime = (createdAt: string) =>
  new Date(createdAt).toLocaleString('ja-JP', {
    year: 'numeric',
    month: 'numeric',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  })

const formatFileSize = (bytes: number): string => {
  if (bytes === 0) {
    return '0 B'
  }

  const units = ['B', 'KB', 'MB', 'GB']
  const unitIndex = Math.min(
    Math.floor(Math.log(bytes) / Math.log(1024)),
    units.length - 1,
  )
  const value = bytes / 1024 ** unitIndex

  return `${new Intl.NumberFormat('ja-JP', {
    maximumSignificantDigits: 3,
  }).format(value)} ${units[unitIndex]}`
}

const createWebpFilename = (filename: string): string => {
  const extension = '.webp'
  const baseName = filename.replace(/\.[^/.]+$/, '') || 'image'
  const maxBaseNameLength = 255 - extension.length

  return `${baseName.slice(0, maxBaseNameLength)}${extension}`
}

const MAX_PDF_FILE_SIZE_BYTES = 10 * 1024 * 1024

function ChatPage() {
  const [sendMessage, setSendMessage] = useState('')
  const [selectedFiles, setSelectedFiles] = useState<File[]>([])
  const [messages, setMessages] = useState<ChatMessages>([])
  const [chatDetail, setChatDetail] = useState<ChatDetail | null>(null)
  const { channelId } = useParams()
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  const messageInputRef = useRef<HTMLTextAreaElement>(null)
  const messagesContainerRef = useRef<HTMLDivElement>(null)
  const [isSending, setIsSending] = useState(false)
  const [sendError, setSendError] = useState('')
  const [submitProgress, setSubmitProgress] = useState('')
  const latestMessageIdRef = useRef<number | null>(null)
  const isPollingRef = useRef(false)
  const [isLoadingOlderMessages, setIsLoadingOlderMessages] = useState(false)
  const [hasOlderMessages, setHasOlderMessages] = useState(false)
  const [olderMessagesError, setOlderMessagesError] = useState('')
  const previousScrollPositionRef = useRef<{
    scrollHeight: number
    scrollTop: number
  } | null>(null)
  const [negotiationSnapshot, setNegotiationSnapshot] =
    useState<ChatNegotiationSnapshot | null>(null)
  const [isLoadingNegotiationSnapshot, setIsLoadingNegotiationSnapshot] =
    useState(true)
  const [negotiationSnapshotError, setNegotiationSnapshotError] = useState('')

  const handleMessageInput = (e: ChangeEvent<HTMLTextAreaElement>) => {
    const textarea = e.currentTarget

    textarea.style.height = 'auto'
    textarea.style.height = `${textarea.scrollHeight}px`
  }

  useEffect(() => {
    if (!channelId) return

    latestMessageIdRef.current = null

    let isCancelled = false

    const loadChatData = async () => {
      try {
        const [detailResult, messagesResult] = await Promise.all([
          getChatDetail(channelId),
          getChatMessages(channelId),
        ])
        const chronologicalMessages = [...messagesResult].reverse()

        if (!isCancelled) {
          setHasOlderMessages(messagesResult.length === 50)
          setError('')
          setChatDetail(detailResult)
          setMessages(chronologicalMessages)
        }
      } catch {
        if (!isCancelled) {
          setChatDetail(null)
          setMessages([])
          setError('チャット情報の取得に失敗しました')
        }
      } finally {
        if (!isCancelled) {
          setIsLoading(false)
        }
      }
    }
    void loadChatData()

    return () => {
      isCancelled = true
    }
  }, [channelId])

  useEffect(() => {
    if (!channelId) return

    let isCancelled = false

    const loadNegotiationSnapshot = async () => {
      try {
        const result = await getChatNegotiationSnapshot(channelId)

        if (!isCancelled) {
          setNegotiationSnapshot(result)
          setNegotiationSnapshotError('')
        }
      } catch {
        if (!isCancelled) {
          setNegotiationSnapshot(null)
          setNegotiationSnapshotError('商談条件の取得に失敗しました。')
        }
      } finally {
        if (!isCancelled) {
          setIsLoadingNegotiationSnapshot(false)
        }
      }
    }

    void loadNegotiationSnapshot()

    return () => {
      isCancelled = true
    }
  }, [channelId])

  useLayoutEffect(() => {
    const container = messagesContainerRef.current

    if (!container || messages.length === 0) return

    const previousPosition = previousScrollPositionRef.current

    if (previousPosition) {
      container.scrollTop =
        previousPosition.scrollTop +
        (container.scrollHeight - previousPosition.scrollHeight)

      previousScrollPositionRef.current = null
      return
    }

    container.scrollTop = container.scrollHeight
  }, [messages])

  useEffect(() => {
    latestMessageIdRef.current = messages[messages.length - 1]?.id ?? null
  }, [messages])

  useEffect(() => {
    if (!channelId) return

    let isCancelled = false

    const pollNewMessages = async () => {
      if (document.visibilityState !== 'visible' || isPollingRef.current) {
        return
      }

      isPollingRef.current = true

      const latestMessageId = latestMessageIdRef.current

      try {
        const receivedMessages =
          latestMessageId === null
            ? await getChatMessages(channelId, { limit: 50 })
            : await getChatMessages(channelId, {
                afterMessageId: latestMessageId,
                limit: 50,
              })

        if (isCancelled || receivedMessages.length === 0) return

        const chronologicalMessages =
          latestMessageId === null
            ? [...receivedMessages].reverse()
            : receivedMessages

        setMessages((prev) => {
          const existingIds = new Set(prev.map((message) => message.id))
          const newMessages = chronologicalMessages.filter(
            (message) => !existingIds.has(message.id),
          )

          return [...prev, ...newMessages]
        })
      } catch {
        // 一時的に失敗しても、次回のポーリングで再試行する
      } finally {
        isPollingRef.current = false
      }
    }

    const intervalId = window.setInterval(() => {
      void pollNewMessages()
    }, 5000)

    return () => {
      isCancelled = true
      window.clearInterval(intervalId)
    }
  }, [channelId])

  const handleLoadOlderMessages = async () => {
    if (!channelId || isLoadingOlderMessages || !hasOlderMessages) {
      return
    }

    const oldestMessageId = messages[0]?.id

    if (oldestMessageId === undefined) {
      setHasOlderMessages(false)
      return
    }

    setOlderMessagesError('')
    setIsLoadingOlderMessages(true)

    try {
      const olderMessages = await getChatMessages(channelId, {
        beforeMessageId: oldestMessageId,
        limit: 50,
      })

      if (olderMessages.length === 0) {
        setHasOlderMessages(false)
        return
      }

      const container = messagesContainerRef.current

      if (container) {
        previousScrollPositionRef.current = {
          scrollHeight: container.scrollHeight,
          scrollTop: container.scrollTop,
        }
      }

      const chronologicalMessages = [...olderMessages].reverse()

      setMessages((prev) => {
        const existingIds = new Set(prev.map((message) => message.id))
        const messagesToAdd = chronologicalMessages.filter(
          (message) => !existingIds.has(message.id),
        )

        return [...messagesToAdd, ...prev]
      })

      setHasOlderMessages(olderMessages.length === 50)
    } catch {
      previousScrollPositionRef.current = null
      setOlderMessagesError('過去のメッセージの取得に失敗しました。')
    } finally {
      setIsLoadingOlderMessages(false)
    }
  }

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const newFiles = Array.from(e.currentTarget.files ?? [])

    setSelectedFiles((prev) => {
      const files = [...prev, ...newFiles]

      return Array.from(
        new Map(
          files.map((file) => [
            `${file.name}-${file.size}-${file.lastModified}`,
            file,
          ]),
        ).values(),
      )
    })

    // 同じファイルをもう一度選択できるようにする
    e.currentTarget.value = ''
  }

  const handleFileRemove = (targetFile: File) => {
    setSelectedFiles((prev) => prev.filter((file) => file !== targetFile))
  }

  const handleSubmit = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()

    if (!canSend || isSending || !channelId) return

    const messageBody = sendMessage.trim()
    const pendingFileUploadIds: string[] = []

    setSendError('')
    setIsSending(true)

    try {
      for (const [index, file] of selectedFiles.entries()) {
        setSubmitProgress(
          `${index + 1}/${selectedFiles.length}件目のファイルを処理しています...`,
        )

        if (file.type === 'application/pdf') {
          if (!file.name.toLowerCase().endsWith('.pdf')) {
            throw new Error('PDFファイルの拡張子を確認してください。')
          }

          if (file.name.length > 255) {
            throw new Error('PDFファイルの名前を255文字以内にしてください。')
          }

          if (file.size > MAX_PDF_FILE_SIZE_BYTES) {
            throw new Error('PDFファイルは10MB以内にしてください。')
          }

          if (file.size === 0) {
            throw new Error('空のPDFファイルは送信できません。')
          }

          const pendingFileUploadId = await uploadPreparedChatFile(
            channelId,
            file,
            file.name,
            'application/pdf',
          )

          pendingFileUploadIds.push(pendingFileUploadId)
          continue
        }

        const webpBlob = await convertImageToWebp(file)
        const webpFilename = createWebpFilename(file.name)

        const pendingFileUploadId = await uploadPreparedChatFile(
          channelId,
          webpBlob,
          webpFilename,
          'image/webp',
        )

        pendingFileUploadIds.push(pendingFileUploadId)
      }

      setSubmitProgress('メッセージを送信しています...')

      await createChatMessage(channelId, {
        body: messageBody || undefined,
        pendingFileUploadIds:
          pendingFileUploadIds.length > 0 ? pendingFileUploadIds : undefined,
      })

      setSendMessage('')
      setSelectedFiles([])

      if (messageInputRef.current) {
        messageInputRef.current.style.height = 'auto'
      }

      try {
        const latestMessageId = latestMessageIdRef.current

        const newMessages =
          latestMessageId === null
            ? await getChatMessages(channelId, { limit: 50 })
            : await getChatMessages(channelId, {
                afterMessageId: latestMessageId,
                limit: 50,
              })

        const chronologicalMessages =
          latestMessageId === null ? [...newMessages].reverse() : newMessages

        setMessages((prev) => {
          const existingIds = new Set(prev.map((message) => message.id))
          const messagesToAdd = chronologicalMessages.filter(
            (message) => !existingIds.has(message.id),
          )

          return [...prev, ...messagesToAdd]
        })
      } catch {
        setSendError('メッセージは送信されましたが、表示の更新に失敗しました。')
      }
    } catch (sendFailure) {
      setSendError(
        sendFailure instanceof Error
          ? sendFailure.message
          : 'メッセージの送信に失敗しました。',
      )
    } finally {
      setSubmitProgress('')
      setIsSending(false)
    }
  }

  const canSend = sendMessage.trim().length > 0 || selectedFiles.length > 0

  if (isLoading) {
    return <p className="py-10 text-center">メッセージを読み込んでいます...</p>
  }

  if (error && !chatDetail) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {error}
      </p>
    )
  }

  return (
    <div className="mx-auto flex max-w-300 w-full h-[calc(100dvh-136px)] min-h-0 flex-none flex-col px-2 text-center md:px-2 lg:flex-row lg:gap-8 lg:px-8 py-2">
      <section className="mx-auto flex flex-1 w-full max-w-5xl flex-col overflow-hidden rounded-xl bg-bg shadow-md ring-1 ring-text/10 min-h-0">
        <header className="border-b border-border/30 bg-textbg/30 p-2 md:p-4 text-left">
          <h2 className="my-0! text-xl! md:text-3xl! truncate">
            {chatDetail?.channelName ?? 'チャット'}
          </h2>

          {chatDetail && (
            <p className="mt-1 text-sm truncate">
              {chatDetail.counterpartyBusinessName}
            </p>
          )}
        </header>

        <div
          className="min-h-0 flex-1 space-y-5 overflow-y-auto bg-textbg/10 p-4 md:p-6"
          ref={messagesContainerRef}
        >
          {/* 過去メッセージ取得ボタン */}
          {hasOlderMessages && messages.length > 0 && (
            <button
              type="button"
              onClick={() => void handleLoadOlderMessages()}
              disabled={isLoadingOlderMessages}
              className="button-base button-secondary mx-auto rounded-full px-4 py-2 text-sm"
            >
              {isLoadingOlderMessages
                ? '過去のメッセージを読み込んでいます...'
                : '過去のメッセージを読み込む'}
            </button>
          )}

          {olderMessagesError && (
            <p role="alert" className="text-center text-sm text-error">
              {olderMessagesError}
            </p>
          )}

          {/* 商談希望送信時と商談開始時の条件比較 */}
          {isLoadingNegotiationSnapshot ? (
            <p className="text-center text-sm text-other">
              商談条件を読み込んでいます...
            </p>
          ) : negotiationSnapshotError ? (
            <p role="alert" className="text-center text-sm text-error">
              {negotiationSnapshotError}
            </p>
          ) : negotiationSnapshot ? (
            <NegotiationSnapshotComparison snapshot={negotiationSnapshot} />
          ) : null}

          {/* メッセージ */}
          {!error && messages.length === 0 && (
            <p className="text-center text-other">
              まだメッセージはありません。
            </p>
          )}

          {messages.map((message) => (
            <article key={message.id}>
              {!message.isMine && (
                <p className="mb-1 text-left text-xs">
                  {message.senderBusinessName}
                </p>
              )}

              <div
                className={
                  message.isMine
                    ? `ml-auto flex w-fit max-w-[85%] flex-col items-start gap-1 
                      lg:w-full lg:max-w-none lg:flex-row-reverse lg:items-end lg:gap-2`
                    : `mr-auto flex w-fit max-w-[85%] flex-col items-end gap-1
                      lg:w-full lg:max-w-none lg:flex-row lg:items-end lg:gap-2`
                }
              >
                <div
                  className={
                    message.isMine
                      ? 'max-w-full rounded-xl bg-textbg p-3 text-left lg:max-w-[70%]'
                      : 'max-w-full rounded-xl bg-bg p-3 text-left shadow-sm ring-1 ring-text/10 lg:max-w-[70%]'
                  }
                >
                  {/* 本文 */}
                  {message.body?.trim() && (
                    <p className="whitespace-pre-wrap wrap-break-word">
                      {message.body}
                    </p>
                  )}
                  {/* 添付ファイル */}
                  {message.messageFiles && message.messageFiles.length > 0 && (
                    <ul className="mt-2 space-y-1 border-t border-border/20 pt-2">
                      {message.messageFiles.map((file, index) => (
                        <li key={`${file.url}-${index}`} className="flex">
                          <span className="shrink-0">添付：</span>
                          <a
                            href={file.url}
                            target="_blank"
                            rel="noreferrer"
                            className="group flex min-w-0 flex-1 text-other"
                            title={file.displayFilename ?? '添付ファイル'}
                          >
                            <span className="min-w-0 truncate border-b border-current group-hover:border-transparent">
                              {file.displayFilename ?? '添付ファイル'}
                            </span>
                            <span className="shrink-0  border-b border-current group-hover:border-transparent">
                              {typeof file.fileSizeBytes === 'number'
                                ? `（${formatFileSize(file.fileSizeBytes)}）`
                                : '（ファイルサイズ不明）'}
                            </span>
                          </a>
                        </li>
                      ))}
                    </ul>
                  )}
                </div>

                <time dateTime={message.createdAt} className="shrink-0 text-xs">
                  {formatChatDateTime(message.createdAt)}
                </time>
              </div>
            </article>
          ))}
          {error && (
            <p role="alert" className="text-center text-error">
              {error}
            </p>
          )}
        </div>

        <form
          onSubmit={handleSubmit}
          aria-busy={isSending}
          className="border-t border-border/30 bg-bg p-3"
        >
          {/* エラー・進捗表示 */}
          {sendError && (
            <p role="alert" className="mb-2 text-sm text-error">
              {sendError}
            </p>
          )}

          {submitProgress && (
            <p
              role="status"
              aria-live="polite"
              className="mb-2 text-sm text-other"
            >
              {submitProgress}
            </p>
          )}
          {/* 添付・入力欄・送信 */}
          <div
            className="flex flex-col gap-2 rounded-xl border border-text/30 bg-bg p-2
          transition focus-within:border-border focus-within:ring-2 focus-within:ring-border/20"
          >
            <textarea
              name="message"
              aria-label="メッセージ"
              rows={1}
              className="min-h-10 max-h-30 md:max-h-20 w-full resize-none rounded-none! border-0! shadow-none! 
              outline-none focus:border-0 focus:outline-none focus:ring-0"
              onChange={(e) => {
                setSendMessage(e.target.value)
                handleMessageInput(e)
              }}
              value={sendMessage}
              ref={messageInputRef}
              disabled={isSending}
            />
            <div className="flex w-full items-center">
              <label
                htmlFor="chat-file"
                className="flex size-10 shrink-0 cursor-pointer items-center justify-center rounded-full button-base button-search textaccent hover:bg-border/70"
              >
                ＋
                <input
                  id="chat-file"
                  type="file"
                  multiple
                  className="sr-only"
                  onChange={handleFileChange}
                  accept="image/png,image/jpeg,image/webp,image/heic,image/heif,application/pdf"
                  disabled={isSending}
                />
              </label>
              {/* 添付したファイルの表示 */}
              {selectedFiles.length > 0 && (
                <div className="flex min-w-0 px-2 flex-1 flex-wrap items-center gap-2">
                  {selectedFiles.map((file) => (
                    <div
                      key={`${file.name}-${file.size}-${file.lastModified}`}
                      className="flex min-w-0 max-w-48 items-center gap-1 rounded-full bg-textbg px-3 py-1"
                    >
                      <span className="min-w-0 flex-1 truncate text-sm">
                        {file.name}
                      </span>

                      <button
                        type="button"
                        aria-label={`${file.name}を削除`}
                        className="shrink-0 disabled:cursor-not-allowed disabled:opacity-50"
                        onClick={() => handleFileRemove(file)}
                        disabled={isSending}
                      >
                        ×
                      </button>
                    </div>
                  ))}
                </div>
              )}

              <button
                type="submit"
                className="shrink-0 ml-auto rounded-full button-search px-4 py-2 text-bg button-base"
                disabled={!canSend || isSending}
              >
                {isSending ? '送信中...' : '送信'}
              </button>
            </div>
          </div>
        </form>
      </section>
    </div>
  )
}

export default ChatPage
