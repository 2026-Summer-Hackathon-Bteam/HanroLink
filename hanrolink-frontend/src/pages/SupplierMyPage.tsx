import mainVisual from '../assets/mainvisual.png'

function SupplierMyPage() {
  return (
    <div className="mx-auto max-w-300 px-4 md:px-6 lg:px-8">
      <h2 className="sr-only">サプライヤーマイページ</h2>
      <p className="my-2">こんにちは、＜サプライヤー名＞様</p>
      {/* 一覧ゾーン */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-[minmax(0,2fr)_minmax(0,1fr)] lg:h-[calc(100dvh-176px)] lg:grid-rows-2">
        {/* 商品希望一覧（受信） */}
        <section className="flex min-h-0 flex-col overflow-hidden min-w-0 lg:col-start-1 lg:row-start-1 rounded-lg bg-bg p-3 shadow-md ring-1 ring-text/5">
          <div className="mb-4 flex flex-col items-start gap-1 sm:justify-between sm:flex-row sm:items-center">
            <h3>商品希望一覧（受信）{`リストの要素数件`}</h3>
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
                <tr className="border-b border-dashed">
                  <td>商品名（データから挿入）</td>
                  <td>バイヤー名（データから挿入）</td>
                  <td>表示期限（データから挿入）</td>
                  <td>
                    <button
                      type="button"
                      className="border border-accent bg-accentbg rounded-full px-2"
                      aria-label='<バイヤー名>から<商品名>への商談希望に承諾'
                    >
                      承諾
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>
        {/* 商品希望一覧（送信） */}
        <section className="flex min-h-0 flex-col overflow-hidden min-w-0 lg:col-start-1 lg:row-start-2 rounded-lg bg-bg p-3 shadow-md  ring-1 ring-text/5">
          <div className="mb-4 flex flex-col items-start gap-1 sm:justify-between sm:flex-row sm:items-center">
            <h3>商品希望一覧（送信）{`リストの要素数件`}</h3>
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
                <tr className="border-b border-dashed">
                  <td>募集情報名（データから挿入）</td>
                  <td>商品名（データから挿入）</td>
                  <td>表示期限（データから挿入）</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>
        {/* 商品情報一覧 */}
        <section className="flex min-h-0 flex-col overflow-hidden min-w-0 lg:col-start-2 lg:row-start-1 rounded-lg bg-bg p-3 shadow-md  ring-1 ring-text/5">
          <div className="mb-4 flex items-center">
            <h3>商品情報一覧 {`リストの要素数件`}</h3>
          </div>
          <button className="bg-border text-bg rounded-lg mb-2 mx-1">
            + 商品情報を登録する（Linkに入れ替える）
          </button>
          <div className="min-h-0 flex-1 space-y-3 overflow-x-hidden overflow-y-auto p-1">
            {/* ここをmapで繰り返す */}
            <article className="flex rounded-lg bg-bg p-3 gap-3 shadow-md  ring-1 ring-text/10">
              <img
                src={mainVisual}
                alt='商品名'
                className="size-20 shrink-0 rounded-md object-cover"
              ></img>
              <div className="flex flex-col justify-between min-w-0 flex-1">
                <div>
                  <h4 className="truncate">
                    商品名商品名商品名商品名商品名商品名
                  </h4>
                  <p className="text-xs">更新日時</p>
                </div>
                <div>
                  <span className="bg-textbg rounded-full px-2 text-sm">
                    非表示中
                  </span>
                </div>
              </div>
            </article>
          </div>
        </section>
        {/* チャット一覧 */}
        <section className="flex min-h-0 flex-col overflow-hidden min-w-0 lg:col-start-2 lg:row-start-2 rounded-lg bg-bg p-3 shadow-md  ring-1 ring-text/5">
          <div className="mb-4 flex items-center">
            <h3>チャット一覧 {`リストの要素数件`}</h3>
          </div>
          <div className="min-h-0 flex-1  overflow-x-hidden overflow-y-auto">
            <table className="w-full table-fixed [&_th]:px-3 [&_th]:py-2 [&_th]:text-left  [&_td]:px-3 [&_td]:py-2 [&_td]:text-left [&_td]:truncate">
              <thead>
                <tr className="border-b">
                  <th scope="col">チャット名</th>
                </tr>
              </thead>
              <tbody>
                <tr className="border-b border-dashed">
                  <td>
                    「商品名_バイヤー名」もしくは「募集情報名_バイヤー名」
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>
      </div>
    </div>
  )
}

export default SupplierMyPage
