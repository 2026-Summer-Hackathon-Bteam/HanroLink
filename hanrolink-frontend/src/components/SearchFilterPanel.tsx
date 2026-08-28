import type { ReactNode, RefObject, SubmitEventHandler } from 'react'

type SearchFilterPanelProps = {
  title: string
  children: ReactNode
  panelRef: RefObject<HTMLElement | null>
  isSearching: boolean
  onSubmit: SubmitEventHandler<HTMLFormElement>
  onReset: () => void
}

function SearchFilterPanel({
  title,
  children,
  panelRef,
  isSearching,
  onSubmit,
  onReset,
}: SearchFilterPanelProps) {
  return (
    <aside
      className="hidden w-full rounded-xl bg-textbg/40 p-4 shadow-md ring-1 ring-text/10 scroll-mt-5 lg:block lg:self-start lg:w-72"
      ref={panelRef}
    >
      <form onSubmit={onSubmit}>
        <h2 className="mt-0! mb-4! py-2 bg-bg text-xl! rounded-full">
          {title}
        </h2>
        {children}
        <div className="mt-5 grid gap-2">
          <button
            type="submit"
            className="rounded-full py-2 font-bold button-base button-search"
            disabled={isSearching}
          >
            {isSearching ? '検索中...' : 'この条件で検索'}
          </button>

          <button
            type="button"
            className="py-1 text-sm text-other underline underline-offset-2 button-base enabled:hover:no-underline"
            onClick={onReset}
            disabled={isSearching}
          >
            条件をリセット
          </button>
        </div>
      </form>
    </aside>
  )
}

export default SearchFilterPanel
