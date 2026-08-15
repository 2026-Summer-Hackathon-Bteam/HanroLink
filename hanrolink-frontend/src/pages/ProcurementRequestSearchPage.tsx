import { useState, useEffect, type SubmitEvent, useRef } from 'react'
import type {
  ProcurementRequestSearchConditions,
  ProcurementRequestSearchOptions,
  ProcurementRequestSearchResult,
} from '../features/procurementRequest/procurementRequestSearchTypes'
import {
  getProcurementRequestSearchData,
  getProcurementRequestSearchOptions,
} from '../features/procurementRequest/procurementRequestSearchService'
import { formatTargetMonth } from '../shared/utils/yearMonth'
import { Link } from 'react-router-dom'
import SearchPagination from '../components/SearchPagination'
import ProductCategoryFilter from '../components/ProductCategoryFilter'
import StorageTypeFilter from '../components/StorageTypeFilter'
import SearchFilterPanel from '../components/SearchFilterPanel'
import MobileSearchFilterDialog from '../components/MobileSearchFilterDialog'

const toggleSelectedValue = <T,>(currentValues: T[], selectedValue: T): T[] => {
  return currentValues.includes(selectedValue)
    ? currentValues.filter((value) => value !== selectedValue)
    : [...currentValues, selectedValue]
}

const initialSearchConditions: ProcurementRequestSearchConditions = {
  keyword: '',
  productCategoryIds: [],
  storageTypes: [],
}

const PAGE_SIZE = 6

function ProcurementRequestSearchPage() {
  const [searchConditions, setSearchConditions] =
    useState<ProcurementRequestSearchConditions>(initialSearchConditions)
  const [searchResult, setSearchResult] =
    useState<ProcurementRequestSearchResult | null>(null)
  const [searchOptions, setSearchOptions] =
    useState<ProcurementRequestSearchOptions | null>(null)
  const [searchError, setSearchError] = useState('')
  const [optionError, setOptionError] = useState('')
  const searchConditionsRef = useRef<HTMLElement>(null)
  const [isSearching, setIsSearching] = useState(false)
  const searchConditionsDialogRef = useRef<HTMLDialogElement>(null)

  useEffect(() => {
    let isCancelled = false

    const loadProcurementRequestSearchOptions = async () => {
      try {
        const result = await getProcurementRequestSearchOptions()

        if (!isCancelled) {
          setSearchOptions(result)
        }
      } catch {
        if (!isCancelled) {
          setOptionError('検索条件の選択肢の取得に失敗しました')
        }
      }
    }

    void loadProcurementRequestSearchOptions()

    return () => {
      isCancelled = true
    }
  }, [])

  useEffect(() => {
    let isCancelled = false

    const loadProcurementRequestSearchResult = async () => {
      try {
        const result = await getProcurementRequestSearchData(
          initialSearchConditions,
          1,
          PAGE_SIZE,
        )

        if (!isCancelled) {
          setSearchResult(result)
        }
      } catch {
        if (!isCancelled) {
          setSearchError('募集情報一覧の取得に失敗しました')
        }
      }
    }

    void loadProcurementRequestSearchResult()

    return () => {
      isCancelled = true
    }
  }, [])

  const handleBackToSearchConditions = () => {
    searchConditionsRef.current?.scrollIntoView({
      behavior: 'smooth',
      block: 'start',
    })
  }

  const handleProductCategoryToggle = (productCategory: number) => {
    setSearchConditions((prev) => ({
      ...prev,
      productCategoryIds: toggleSelectedValue(
        prev.productCategoryIds,
        productCategory,
      ),
    }))
  }

  const handleStorageTypeToggle = (
    storageType: ProcurementRequestSearchConditions['storageTypes'][number],
  ) => {
    setSearchConditions((prev) => ({
      ...prev,
      storageTypes: toggleSelectedValue(prev.storageTypes, storageType),
    }))
  }

  const handleOpenSearchConditions = () => {
    const dialog = searchConditionsDialogRef.current
    if (!dialog) return

    dialog.showModal()

    dialog.animate(
      [{ transform: 'translateY(100%)' }, { transform: 'translateY(0)' }],
      {
        duration: 150,
        easing: 'ease-out',
      },
    )
  }

  const handleCloseSearchConditions = () => {
    const dialog = searchConditionsDialogRef.current
    if (!dialog) return

    const animation = dialog.animate(
      [{ transform: 'translateY(0)' }, { transform: 'translateY(100%)' }],
      {
        duration: 100,
        easing: 'ease-in',
      },
    )

    void animation.finished.then(() => {
      dialog.close()
      animation.cancel()
    })
  }

  const handleSearch = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()

    if (isSearching) return

    setSearchError('')
    setIsSearching(true)

    try {
      const result = await getProcurementRequestSearchData(
        searchConditions,
        1,
        PAGE_SIZE,
      )
      setSearchResult(result)

      if (searchConditionsDialogRef.current?.open) {
        handleCloseSearchConditions()
      }
    } catch {
      setSearchError('検索結果の取得に失敗しました。')
    } finally {
      setIsSearching(false)
    }
  }

  const handlePageChange = async (page: number) => {
    if (!searchResult || isSearching) return

    if (
      page < 1 ||
      page > searchResult.pagination.totalPages ||
      page === searchResult.pagination.page
    ) {
      return
    }

    setSearchError('')
    setIsSearching(true)

    try {
      const result = await getProcurementRequestSearchData(
        searchConditions,
        page,
        searchResult.pagination.pageSize,
      )

      setSearchResult(result)
    } catch {
      setSearchError('検索結果の取得に失敗しました。')
    } finally {
      setIsSearching(false)
    }
  }

  if (optionError) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {optionError}
      </p>
    )
  }

  if (searchError && !searchResult) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {searchError}
      </p>
    )
  }

  if (!searchOptions || !searchResult) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  const renderSearchConditionFields = () => (
    <>
      <div className="pb-4">
        <h3 className="mb-4 border-b-2 border-border px-2 text-left font-bold text-border textaccent">
          タイトル・説明文
        </h3>
        <div className="pb-4">
          <label className="block">
            <span className="sr-only">タイトル・説明文</span>
            <input
              type="text"
              name="keyword"
              value={searchConditions.keyword}
              onChange={(e) =>
                setSearchConditions((prev) => ({
                  ...prev,
                  keyword: e.target.value,
                }))
              }
              className="bg-bg w-full"
            />
          </label>
        </div>
      </div>

      <ProductCategoryFilter
        categoryGroups={searchOptions.productCategoryGroups}
        categories={searchOptions.productCategories}
        selectedCategoryIds={searchConditions.productCategoryIds}
        onToggle={handleProductCategoryToggle}
      />

      <StorageTypeFilter
        storageTypes={searchOptions.storageTypes}
        selectedStorageTypes={searchConditions.storageTypes}
        onToggle={handleStorageTypeToggle}
      />
    </>
  )

  return (
    <>
      <div className="mx-auto flex max-w-300 flex-col px-4 text-center md:px-6 lg:flex-row lg:gap-8 lg:px-8 lg:py-6">
        <SearchFilterPanel
          title="募集情報検索"
          panelRef={searchConditionsRef}
          isSearching={isSearching}
          onSubmit={handleSearch}
          onReset={() => setSearchConditions(initialSearchConditions)}
        >
          {renderSearchConditionFields()}
        </SearchFilterPanel>

        <section className="min-w-0 flex-1 pb-20 lg:pb-0">
          <div>
            <p className="px-2 text-left border-b-10 border-textbg mb-8">
              <span className="text-4xl mr-1">
                {searchResult.pagination.totalCount}
              </span>
              件の募集情報が見つかりました
            </p>
          </div>
          <div>
            {searchError && (
              <p role="alert" className="mb-4 text-left text-error">
                {searchError}
              </p>
            )}
            {searchResult.procurementRequests.length === 0 ? (
              <div
                role="status"
                className="rounded-xl bg-textbg/30 px-6 py-12 text-center"
              >
                <p className="mb-4 text-lg">
                  条件に一致する募集情報が見つかりませんでした。
                </p>

                <button
                  type="button"
                  onClick={handleBackToSearchConditions}
                  className="hidden text-border underline underline-offset-2 hover:no-underline lg:inline"
                >
                  検索条件を変更する
                </button>
              </div>
            ) : (
              <div className="flex flex-col gap-7">
                {searchResult.procurementRequests.map((request, index) => (
                  <div key={request.id}>
                    <Link
                      to={`/procurement-requests/${request.id}`}
                      className="group block rounded-2xl focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-border"
                    >
                      <article className="md:p-6 p-4 rounded-2xl shadow-[0_1px_2px_0.5px_rgb(0_0_0/0.30)] transition duration-300 group-hover:-translate-y-0.5 group-hover:shadow-lg">
                        <div className="pb-4">
                          <p className="text-left">
                            {request.buyer.businessName}
                          </p>
                          <h3 className="text-2xl text-left mb-2">
                            {request.title}
                          </h3>
                          <p className="text-left line-clamp-2 mb-2">
                            {request.description}
                          </p>
                          <div className="flex flex-wrap gap-2">
                            <span className="px-3 py-1 rounded-full bg-badgesto">
                              {request.storageTypeLabels.join('・')}
                            </span>
                            <span className="px-3 py-1 rounded-full bg-badgecate">
                              {request.productCategoryName}
                            </span>
                          </div>
                        </div>
                        <div className="overflow-hidden border border-border">
                          {/* スマホ・タブレット用の見出し */}
                          <div className="grid grid-cols-[8rem_minmax(0,1fr)] border-b border-border bg-textbg md:hidden">
                            <div className="border-r border-border px-3 py-2 text-left">
                              希望月
                            </div>
                            <div className="px-3 py-2 text-center">
                              希望数量
                            </div>
                          </div>

                          <div className="md:grid md:grid-cols-[9rem_repeat(6,minmax(0,1fr))]">
                            {/* PC用の左側の項目名 */}
                            <div className="hidden bg-textbg md:grid md:grid-rows-2">
                              <div className="flex items-center border-b border-border px-3 py-2 text-left text-sm">
                                希望月
                              </div>
                              <div className="flex items-center px-3 py-2 text-left text-sm">
                                希望数量
                              </div>
                            </div>

                            {request.monthlyProcurementQuantities.map(
                              (quantity) => {
                                return (
                                  <div
                                    key={quantity.targetMonth}
                                    className="
                                              grid grid-cols-[8rem_minmax(0,1fr)]
                                              border-b border-border last:border-b-0
                                              md:grid-cols-1 md:grid-rows-2
                                              md:border-b-0 md:border-l
                                            "
                                  >
                                    <div
                                      className="
                                                flex items-center whitespace-nowrap bg-textbg px-2 py-1 text-left text-sm
                                                border-r border-border
                                                md:justify-center md:bg-bg
                                                md:border-r-0 md:border-b md:text-center
                                              "
                                    >
                                      {formatTargetMonth(quantity.targetMonth)}
                                    </div>

                                    <div className="flex justify-center items-center px-2 py-1 text-sm bg-bg">
                                      {quantity.desiredQuantity}
                                    </div>
                                  </div>
                                )
                              },
                            )}
                          </div>
                        </div>
                      </article>
                    </Link>
                    {(index + 1) % 5 === 0 &&
                      index !== searchResult.procurementRequests.length - 1 && (
                        <div className="hidden lg:flex lg:justify-end">
                          <button
                            type="button"
                            onClick={handleBackToSearchConditions}
                            className="mt-1 underline underline-offset-2 hover:no-underline"
                          >
                            検索条件に戻る
                          </button>
                        </div>
                      )}
                  </div>
                ))}
              </div>
            )}
          </div>
        </section>
      </div>
      {searchResult.procurementRequests.length > 0 && (
        <SearchPagination
          currentPage={searchResult.pagination.page}
          totalPages={searchResult.pagination.totalPages}
          isLoading={isSearching}
          onPageChange={handlePageChange}
        />
      )}
      <button
        type="button"
        onClick={handleOpenSearchConditions}
        className="
        fixed right-4 bottom-[calc(env(safe-area-inset-bottom)+1rem)]
        z-40 rounded-full bg-border px-5 py-3
        font-bold text-bg shadow-lg
        lg:hidden
        "
      >
        検索条件を変更
      </button>

      <MobileSearchFilterDialog
        title="募集情報"
        dialogRef={searchConditionsDialogRef}
        isSearching={isSearching}
        onSubmit={handleSearch}
        onReset={() => setSearchConditions(initialSearchConditions)}
        onClose={handleCloseSearchConditions}
      >
        {renderSearchConditionFields()}
      </MobileSearchFilterDialog>

      {isSearching && (
        <p role="status" aria-live="polite" className="mt-4 text-center">
          読み込み中...
        </p>
      )}
    </>
  )
}

export default ProcurementRequestSearchPage
