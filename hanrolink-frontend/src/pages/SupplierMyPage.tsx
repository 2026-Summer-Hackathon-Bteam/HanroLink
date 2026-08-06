import { useEffect, useState } from 'react'
import { getSupplierMyPageData } from '../features/supplier/supplierMyPageService'
import type { SupplierMyPageData } from '../features/supplier/supplierMyPageTypes'
import { Link } from 'react-router-dom'

function SupplierMyPage() {
  const [data, setData] = useState<SupplierMyPageData | null>(null)
  const [error, setError] = useState('')

  useEffect(() => {
    let isCancelled = false

    const loadSupplierMyPageData = async () => {
      try {
        const result = await getSupplierMyPageData()

        if (!isCancelled) {
          setData(result)
        }
      } catch {
        if (!isCancelled) {
          setError('マイページの情報を取得できませんでした。')
        }
      }
    }

    void loadSupplierMyPageData()

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
    <div className="mx-auto max-w-300 px-4 md:px-6 lg:px-8">
      <h2 className="sr-only">サプライヤーマイページ</h2>
      <p className="my-2">こんにちは、{data.business.businessName}様</p>
      {/* 一覧ゾーン */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-[minmax(0,2fr)_minmax(0,1fr)] lg:h-[calc(100dvh-176px)] lg:grid-rows-2">
        {/* 商談希望一覧（受信） */}
        <section className="flex min-h-0 flex-col overflow-hidden min-w-0 lg:col-start-1 lg:row-start-1 rounded-lg bg-bg p-3 shadow-md ring-1 ring-text/5">
          <div className="mb-4 flex flex-col items-start gap-1 sm:justify-between sm:flex-row sm:items-center">
            <h3>
              商談希望一覧（受信）：{`${data.receivedNegotiations.length}件`}
            </h3>
            <p className="text-xs">
              承諾ボタンを押すと商談希望を送信したバイヤーとのチャットが作成されます
            </p>
          </div>
          <div className="min-h-0 flex-1  overflow-x-hidden overflow-y-auto">
            <table className="w-full table-fixed [&_th]:px-3 [&_th]:py-2 [&_th]:text-left  [&_td]:px-3 [&_td]:py-2 [&_td]:text-left [&_td]:truncate">
              <colgroup>
                <col />
                <col />
                <col />
                <col className="w-20" />
              </colgroup>
              <thead>
                <tr className="border-b">
                  <th scope="col">商品名</th>
                  <th scope="col">バイヤー名</th>
                  <th scope="col">表示期限</th>
                  <th scope="col">
                    <span className="sr-only">承諾</span>
                  </th>
                </tr>
              </thead>
              <tbody>
                {/* ここをmapで繰り返す */}
                {data.receivedNegotiations.length === 0 ? (
                  <tr>
                    <td colSpan={4} className="py-4 text-center!">
                      まだ商談希望は届いていないか、期限が切れました。
                    </td>
                  </tr>
                ) : (
                  [...data.receivedNegotiations]
                    .sort(
                      (a, b) =>
                        new Date(b.expiresAt).getTime() -
                        new Date(a.expiresAt).getTime(),
                    )
                    .map((rn) => (
                      <tr
                        key={rn.productNegotiationRequestId}
                        className="border-b border-dashed"
                      >
                        <td>
                          <Link to={`/products/${rn.product.id}`}>
                            {rn.product.name}
                          </Link>
                        </td>
                        <td>
                          <Link to={`/buyer/${rn.buyer.accountId}`}>
                            {rn.buyer.businessName}
                          </Link>
                        </td>
                        <td>
                          {new Date(rn.expiresAt).toLocaleString('ja-JP')}
                        </td>
                        <td>
                          <button
                            type="button"
                            className="border border-accent bg-accentbg rounded-full px-2"
                            aria-label={`${rn.buyer.businessName}から届いた${rn.product.name}への商談希望を承諾`}
                          >
                            承諾
                          </button>
                        </td>
                      </tr>
                    ))
                )}
              </tbody>
            </table>
          </div>
        </section>
        {/* 商談希望一覧（送信） */}
        <section className="flex min-h-0 flex-col overflow-hidden min-w-0 lg:col-start-1 lg:row-start-2 rounded-lg bg-bg p-3 shadow-md  ring-1 ring-text/5">
          <div className="mb-4 flex flex-col items-start gap-1 sm:justify-between sm:flex-row sm:items-center">
            <h3>商談希望一覧（送信）：{`${data.sentNegotiations.length}件`}</h3>
            <p className="text-xs">
              募集情報を作成したバイヤーが承諾するとチャットが作成されます
            </p>
          </div>
          <div className="min-h-0 flex-1 overflow-x-hidden overflow-y-auto">
            <table className="w-full table-fixed [&_th]:px-3 [&_th]:py-2 [&_th]:text-left  [&_td]:px-3 [&_td]:py-2 [&_td]:text-left [&_td]:truncate">
              <thead>
                <tr className="border-b">
                  <th scope="col">募集情報名</th>
                  <th scope="col">商品名</th>
                  <th scope="col">表示期限</th>
                </tr>
              </thead>
              <tbody>
                {/* ここをmapで繰り返す */}
                {data.sentNegotiations.length === 0 ? (
                  <tr>
                    <td colSpan={3} className="py-4 text-center!">
                      まだ商談希望を送信していないか、期限が切れました
                    </td>
                  </tr>
                ) : (
                  [...data.sentNegotiations]
                    .sort(
                      (a, b) =>
                        new Date(b.expiresAt).getTime() -
                        new Date(a.expiresAt).getTime(),
                    )
                    .map((sn) => (
                      <tr
                        key={sn.procurementNegotiationRequestId}
                        className="border-b border-dashed"
                      >
                        <td>
                          <Link
                            to={`/recruitments/${sn.procurementRequest.id}`}
                          >
                            {sn.procurementRequest.title}
                          </Link>
                        </td>
                        <td>
                          <Link to={`/products/${sn.product.id}`}>
                            {sn.product.name}
                          </Link>
                        </td>
                        <td>
                          {new Date(sn.expiresAt).toLocaleString('ja-JP')}
                        </td>
                      </tr>
                    ))
                )}
              </tbody>
            </table>
          </div>
        </section>
        {/* 商品情報一覧 */}
        <section className="flex min-h-0 flex-col overflow-hidden min-w-0 lg:col-start-2 lg:row-start-1 rounded-lg bg-bg p-3 shadow-md  ring-1 ring-text/5">
          <div className="mb-4 flex items-center">
            <h3>商品情報一覧： {`${data.products.length}件`}</h3>
          </div>
          <Link to={'/products/new'} className="bg-border text-bg rounded-lg mb-2 mx-1 text-center py-1">
            + 商品情報を登録する
          </Link>
          <div className="min-h-0 flex-1 space-y-3 overflow-x-hidden overflow-y-auto p-1">
            {/* ここをmapで繰り返す */}
            {data.products.length === 0 ? (
              <p className='text-center'>まずは商品を登録しましょう！</p>
            ) : (
              [...data.products]
                .sort(
                  (a, b) =>
                    new Date(b.updatedAt).getTime() -
                    new Date(a.updatedAt).getTime(),
                )
                .map((product) => (
                  <article
                    key={product.id}
                    className="rounded-lg bg-bg p-3 shadow-md  ring-1 ring-text/10"
                  >
                    <Link to={`/products/${product.id}`} className="flex gap-3">
                      <img
                        src={product.mainImageUrl}
                        alt={product.name}
                        className="size-20 shrink-0 rounded-md object-cover"
                      ></img>
                      <div className="flex flex-col justify-between min-w-0 flex-1">
                        <div>
                          <h4 className="truncate">{product.name}</h4>
                          <p className="text-xs">
                            更新日時：
                            {new Date(product.updatedAt).toLocaleString(
                              'ja-JP',
                            )}
                          </p>
                        </div>
                        <div>
                          {product.hidden && (
                            <span className="bg-textbg rounded-full px-2 text-sm">
                              非表示中
                            </span>
                          )}
                        </div>
                      </div>
                    </Link>
                  </article>
                ))
            )}
          </div>
        </section>
        {/* チャット一覧 */}
        <section className="flex min-h-0 flex-col overflow-hidden min-w-0 lg:col-start-2 lg:row-start-2 rounded-lg bg-bg p-3 shadow-md  ring-1 ring-text/5">
          <div className="mb-4 flex items-center">
            <h3>チャット一覧 ：{`${data.chats.length}件`}</h3>
          </div>
          <div className="min-h-0 flex-1  overflow-x-hidden overflow-y-auto">
            <table className="w-full table-fixed [&_th]:px-3 [&_th]:py-2 [&_th]:text-left  [&_td]:px-3 [&_td]:py-2 [&_td]:text-left [&_td]:truncate">
              <thead>
                <tr className="border-b">
                  <th scope="col">チャット名</th>
                </tr>
              </thead>
              <tbody>
                {data.chats.length === 0 ? (
                  <tr>
                    <td colSpan={1} className="py-4 text-center!">
                      まだチャットがありません。
                    </td>
                  </tr>
                ) : (
                  [...data.chats]
                    .sort(
                      (a, b) =>
                        new Date(b.lastActivityAt).getTime() -
                        new Date(a.lastActivityAt).getTime(),
                    )
                    .map((chat) => (
                      <tr key={chat.id} className="border-b border-dashed">
                        <td>{chat.name}</td>
                      </tr>
                    ))
                )}
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </div>
  )
}

export default SupplierMyPage
