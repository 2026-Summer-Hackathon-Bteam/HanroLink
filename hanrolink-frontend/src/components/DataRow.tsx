import type { ReactNode } from 'react'

type DataRowProps = {
  itemName: string
  children: ReactNode
}

function DataRow({ itemName, children }: DataRowProps) {
  return (
    <div className="grid md:grid-cols-[16rem_1fr]">
      <dt className="flex items-center bg-textbg px-5 py-4 md:border-r md:border-border text-left">
        {itemName}
      </dt>

      <dd className="flex min-w-0 items-center gap-4 wrap-break-word p-3 text-start">{children}</dd>
    </div>
  )
}

export default DataRow
