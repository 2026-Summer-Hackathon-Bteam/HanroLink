type SearchPaginationProps = {
  currentPage: number
  totalPages: number
  isLoading: boolean
  onPageChange: (page: number) => void | Promise<void>
}

function SearchPagination({
  currentPage,
  totalPages,
  isLoading,
  onPageChange,
}: SearchPaginationProps) {
  if (totalPages <= 1) return null

  return (
    <nav
      aria-label="検索結果のページ"
      className="mt-8 flex items-center justify-center gap-2"
    >
      <button
        type="button"
        disabled={currentPage === 1 || isLoading}
        onClick={() => void onPageChange(currentPage - 1)}
        className="rounded-full border border-border px-3 py-2 disabled:cursor-not-allowed disabled:opacity-50"
      >
        前へ
      </button>

      {Array.from({ length: totalPages }, (_, index) => index + 1).map(
        (page) => {
          const isCurrentPage = page === currentPage

          return (
            <button
              key={page}
              type="button"
              aria-current={isCurrentPage ? 'page' : undefined}
              onClick={() => void onPageChange(page)}
              className={
                isCurrentPage
                  ? 'size-10 rounded-full bg-border font-bold text-bg'
                  : 'size-10 rounded-full border border-border hover:bg-textbg disabled:cursor-not-allowed disabled:opacity-50'
              }
              disabled={isLoading || isCurrentPage}
            >
              {page}
            </button>
          )
        },
      )}

      <button
        type="button"
        disabled={currentPage === totalPages || isLoading}
        onClick={() => void onPageChange(currentPage + 1)}
        className="rounded-full border border-border px-3 py-2 disabled:cursor-not-allowed disabled:opacity-50"
      >
        次へ
      </button>
    </nav>
  )
}

export default SearchPagination
