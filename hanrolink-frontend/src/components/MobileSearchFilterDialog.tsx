import type { ReactNode, RefObject, SubmitEventHandler } from 'react'

type MobileSearchFilterDialogProps = {
  title: string
  children: ReactNode
  dialogRef: RefObject<HTMLDialogElement | null>
  isSearching: boolean
  onSubmit: SubmitEventHandler<HTMLFormElement>
  onReset: () => void
  onClose: () => void
}

function MobileSearchFilterDialog({
  title,
  children,
  dialogRef,
  isSearching,
  onSubmit,
  onReset,
  onClose,
}: MobileSearchFilterDialogProps) {
  return (
    <dialog
      ref={dialogRef}
      aria-labelledby="mobile-search-conditions-title"
      className="
          fixed inset-x-0 top-auto bottom-0
          m-0 h-[90dvh] max-h-[90dvh] w-full max-w-none
          rounded-t-2xl bg-bg p-0
         backdrop:bg-black/40
          lg:hidden
        "
      onCancel={(e) => {
        e.preventDefault()
        onClose()
      }}
    >
      <form onSubmit={onSubmit} className="flex h-full flex-col">
        <div className="flex shrink-0 items-center justify-between border-b border-border px-4 py-3">
          <h2 id="mobile-search-conditions-title" className="m-0! text-xl!">
            {`${title}検索条件`}
          </h2>

          <button
            type="button"
            aria-label="検索条件を閉じる"
            onClick={onClose}
            className="flex size-10 items-center justify-center rounded-full text-2xl pb-1 hover:bg-textbg"
          >
            ×
          </button>
        </div>

        <div className="min-h-0 flex-1 overflow-y-auto p-4">{children}</div>

        <div className="shrink-0 border-t border-border bg-bg p-4">
          <div className="grid grid-cols-2 gap-3">
            <button
              type="button"
              disabled={isSearching}
              onClick={onReset}
              className="rounded-full border border-border py-2 disabled:cursor-not-allowed disabled:opacity-50"
            >
              条件をリセット
            </button>

            <button
              type="submit"
              disabled={isSearching}
              className="rounded-full bg-border py-2 font-bold text-bg disabled:cursor-not-allowed disabled:opacity-50"
            >
              {isSearching ? '検索中...' : 'この条件で検索'}
            </button>
          </div>
        </div>
      </form>
    </dialog>
  )
}

export default MobileSearchFilterDialog
