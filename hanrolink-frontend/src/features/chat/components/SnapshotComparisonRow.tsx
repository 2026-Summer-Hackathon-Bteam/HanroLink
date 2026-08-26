import type { ReactNode } from 'react'

type SnapshotComparisonRowProps = {
  label: string
  requestedValue: ReactNode
  acceptedValue: ReactNode
  changed: boolean
}

function SnapshotComparisonRow({
  label,
  requestedValue,
  acceptedValue,
  changed,
}: SnapshotComparisonRowProps) {
  return (
    <div className="grid md:grid-cols-[12rem_minmax(0,1fr)_minmax(0,1fr)]">
      <dt className="bg-textbg/30 px-3 py-2 font-medium md:flex md:items-center md:border-r md:border-border/30">
        {label}
      </dt>

      {changed ? (
        <>
          <dd className="min-w-0 px-3 py-2">
            <span className="mb-1 block text-xs font-medium text-other md:hidden">
              商談希望送信時
            </span>

            <div className="whitespace-pre-wrap wrap-break-word">
              {requestedValue}
            </div>
          </dd>

          <dd className="min-w-0 border-t border-border/30 bg-accentbg/30 px-3 py-2 md:border-t-0 md:border-l">
            <span className="mb-1 block text-xs font-medium text-other md:hidden">
              商談開始時
            </span>

            <div className="whitespace-pre-wrap wrap-break-word">
              {acceptedValue}
            </div>
          </dd>
        </>
      ) : (
        <dd className="min-w-0 px-3 py-2 md:col-span-2">
          <div className="whitespace-pre-wrap wrap-break-word">
            {acceptedValue}
          </div>
        </dd>
      )}
    </div>
  )
}

export default SnapshotComparisonRow
