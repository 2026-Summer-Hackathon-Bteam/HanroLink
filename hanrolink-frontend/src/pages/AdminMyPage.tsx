import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { getPendingBusinessRegistrations } from '../features/admin/adminMyPageService'
import type { PendingBusinessRegistration } from '../features/admin/adminMyPageTypes'

const REVIEW_PERIOD_MS = 3 * 24 * 60 * 60 * 1000

const formatRemainingTime = (createdAt: string): string => {
  const createdAtMs = new Date(createdAt).getTime()

  if (Number.isNaN(createdAtMs)) {
    return '日時不明'
  }

  const deadlineMs = createdAtMs + REVIEW_PERIOD_MS
  const remainingMs = deadlineMs - Date.now()

  if (remainingMs <= 0) {
    return '期限超過'
  }

  const totalMinutes = Math.ceil(remainingMs / (60 * 1000))
  const days = Math.floor(totalMinutes / (24 * 60))
  const hours = Math.floor((totalMinutes % (24 * 60)) / 60)
  const minutes = totalMinutes % 60

  return `${days}日 ${hours}時間 ${minutes}分`
}

function AdminMyPage() {
  const [data, setData] = useState<PendingBusinessRegistration[] | null>(null)
  const [error, setError] = useState('')

  useEffect(() => {
    let isCancelled = false

    const loadAdminMyPageData = async () => {
      try {
        const result = await getPendingBusinessRegistrations()

        if (!isCancelled) {
          setData(result)
        }
      } catch (error: unknown) {
        if (!isCancelled) {
          setError(
            error instanceof Error
              ? error.message
              : 'マイページの情報を取得できませんでした',
          )
        }
      }
    }

    void loadAdminMyPageData()

    return () => {
      isCancelled = true
    }
  }, [])

  if (error) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {error}
      </p>
    )
  }

  if (!data) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  return (
    <div className="mx-auto flex min-h-0 w-full max-w-300 flex-1 flex-col px-4 text-center md:px-6 lg:px-8 lg:h-[calc(100dvh-136px)] lg:flex-none">
      <h2 className="sr-only">管理者マイページ</h2>
      <div className="flex justify-center mt-3 min-h-0 flex-1">
        <section className="flex flex-col min-h-0 w-3/4 rounded-lg bg-bg p-3 shadow-md ring-1 ring-text/5">
          <div className="flex mb-4 justify-start">
            <h3>新規登録者一覧：{`${data.length}件`}</h3>
          </div>
          <div className="min-h-0 flex-1 overflow-y-auto">
            <table className="w-full table-fixed [&_th]:px-3 [&_th]:py-2 [&_th]:text-left  [&_td]:px-3 [&_td]:py-2 [&_td]:text-left [&_td]:truncate">
              <thead>
                <tr className="border-b">
                  <th>事業者名</th>
                  <th>登録日時</th>
                  <th>審査残り時間</th>
                </tr>
              </thead>
              <tbody>
                {data.length === 0 ? (
                  <tr>
                    <td colSpan={3} className="py-4 text-center!">
                      現在、審査待ち状態の新規登録者情報がありません。
                    </td>
                  </tr>
                ) : (
                  [...data]
                    .sort(
                      (a, b) =>
                        new Date(a.createdAt).getTime() -
                        new Date(b.createdAt).getTime(),
                    )
                    .map((business) => {
                      const remainingTime = formatRemainingTime(business.createdAt)
                      const isExpired = remainingTime === '期限超過'

                      return (
                        <tr
                          key={business.businessId}
                          className="border-b border-dashed"
                        >
                          <td>
                            <Link
                              to={`/admin/approvals/${business.businessId}`}
                            >
                              {business.businessName}
                            </Link>
                          </td>
                          <td>{new Date(business.createdAt).toLocaleString()}</td>
                          <td className={isExpired ? 'text-error' : undefined}>
                            {remainingTime}
                          </td>
                        </tr>
                      )
                    })
                )}
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </div>
  )
}

export default AdminMyPage
