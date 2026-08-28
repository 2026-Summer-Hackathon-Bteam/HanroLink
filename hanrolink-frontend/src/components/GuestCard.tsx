type GuestCardProps = {
  name: string
  supplierName: string
  mainImageUrl: string
}

function GuestCard({ name, supplierName, mainImageUrl }: GuestCardProps) {
  return (
    <article className="min-h-95 w-full max-w-90 p-4 rounded-2xl shadow-[0_1px_2px_0.5px_rgb(0_0_0/0.30)]">
      <div className="h-60 bg-accentbg mb-2 rounded-xl">
        <img
          src={mainImageUrl}
          alt={`${name}の商品画像`}
          className="h-full w-full rounded-xl object-cover"
        />
      </div>
      <p>{supplierName}</p>
      <h3 className="text-xl mb-2">{name}</h3>
      <dl className="divide-y divide-border border border-border">
        <div className="grid grid-cols-[7.5rem_1fr]">
          <dt className="border-r border-border bg-textbg px-2 py-1">
            提供可能時期
          </dt>
          <dd className="px-2 py-1 text-center">ログイン後に表示</dd>
        </div>

        <div className="grid grid-cols-[7.5rem_1fr]">
          <dt className="border-r border-border bg-textbg px-2 py-1">
            提供可能数量
          </dt>
          <dd className="px-2 py-1 text-center">ログイン後に表示</dd>
        </div>
      </dl>
    </article>
  )
}

export default GuestCard
