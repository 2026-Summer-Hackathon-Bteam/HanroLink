import {
  useState,
  type ChangeEvent,
  useRef,
  type SubmitEvent,
  useEffect,
  useLayoutEffect,
} from 'react'
import { useParams } from 'react-router-dom'
import type { ChatDetail, ChatMessages } from '../features/chat/ChatTypes'
import { getChatDetail, getChatMessages } from '../features/chat/ChatService'

const formatChatDateTime = (createdAt: string) =>
  new Date(createdAt).toLocaleString('ja-JP', {
    year: 'numeric',
    month: 'numeric',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  })

function formatFileSize(bytes: number): string {
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

  const handleMessageInput = (e: ChangeEvent<HTMLTextAreaElement>) => {
    const textarea = e.currentTarget

    textarea.style.height = 'auto'
    textarea.style.height = `${textarea.scrollHeight}px`
  }

  useEffect(() => {
    if (!channelId) return

    let isCancelled = false

    const loadChatData = async () => {
      try {
        const [detailResult, messagesResult] = await Promise.all([
          getChatDetail(channelId),
          getChatMessages(channelId),
        ])

        if (!isCancelled) {
          setError('')
          setChatDetail(detailResult)
          setMessages(messagesResult)
        }
      } catch {
        if (!isCancelled) {
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

  useLayoutEffect(() => {
    const container = messagesContainerRef.current

    if (!container || messages.length === 0) return

    container.scrollTop = container.scrollHeight
  }, [messages])

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

  const handleSubmit = (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()

    if (!canSend) return
    //送信処理を書く
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
            {chatDetail?.name ?? 'チャット'}
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
          className="border-t border-border/30 bg-bg p-3"
        >
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
            />
            <div className="flex w-full items-center">
              <label
                htmlFor="chat-file"
                className="flex size-10 shrink-0 cursor-pointer items-center justify-center rounded-full bg-border text-bg textaccent hover:bg-border/70"
              >
                ＋
                <input
                  id="chat-file"
                  type="file"
                  multiple
                  className="sr-only"
                  onChange={handleFileChange}
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
                        className="shrink-0"
                        onClick={() => handleFileRemove(file)}
                      >
                        ×
                      </button>
                    </div>
                  ))}
                </div>
              )}

              <button
                type="submit"
                className="shrink-0 ml-auto rounded-full bg-border px-4 py-2 text-bg disabled:cursor-not-allowed disabled:opacity-50"
                disabled={!canSend}
              >
                送信
              </button>
            </div>
          </div>
        </form>
      </section>
    </div>
  )
}

export default ChatPage
