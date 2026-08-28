import { useState, useEffect, type SubmitEvent, useRef } from 'react'
import type {
  ProductSearchResult,
  ProductSearchConditions,
  ProductSearchOptions,
} from '../features/product/productSearchTypes'
import {
  getProductSearchData,
  getProductSearchOptions,
} from '../features/product/productSearchService'
import { formatTargetMonth } from '../shared/utils/yearMonth'
import { createTargetMonths } from '../shared/utils/yearMonth'
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

const initialSearchConditions: ProductSearchConditions = {
  targetMonths: [],
  productCategoryIds: [],
  storageTypes: [],
  mainIngredientRegionIds: [],
}

const PAGE_SIZE = 20

function ProductSearchPage() {
  const [searchConditions, setSearchConditions] =
    useState<ProductSearchConditions>(initialSearchConditions)
  const [searchResult, setSearchResult] = useState<ProductSearchResult | null>(
    null,
  )
  const [searchOptions, setSearchOptions] =
    useState<ProductSearchOptions | null>(null)
  const [searchError, setSearchError] = useState('')
  const [optionError, setOptionError] = useState('')
  const [targetMonths] = useState(createTargetMonths)
  const searchConditionsRef = useRef<HTMLElement>(null)
  const [isSearching, setIsSearching] = useState(false)
  const searchConditionsDialogRef = useRef<HTMLDialogElement>(null)
  const [appliedSearchConditions, setAppliedSearchConditions] =
    useState<ProductSearchConditions>(initialSearchConditions)

  useEffect(() => {
    let isCancelled = false

    const loadProductSearchOptions = async () => {
      try {
        const result = await getProductSearchOptions()

        if (!isCancelled) {
          setSearchOptions(result)
        }
      } catch {
        if (!isCancelled) {
          setOptionError('検索条件の選択肢の取得に失敗しました')
        }
      }
    }

    void loadProductSearchOptions()

    return () => {
      isCancelled = true
    }
  }, [])

  useEffect(() => {
    let isCancelled = false

    const loadProductSearchResult = async () => {
      try {
        const result = await getProductSearchData(
          initialSearchConditions,
          1,
          PAGE_SIZE,
        )

        if (!isCancelled) {
          setSearchResult(result)
        }
      } catch {
        if (!isCancelled) {
          setSearchError('商品一覧の取得に失敗しました')
        }
      }
    }

    void loadProductSearchResult()

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

  const handleTargetMonthToggle = (targetMonth: string) => {
    setSearchConditions((prev) => ({
      ...prev,
      targetMonths: toggleSelectedValue(prev.targetMonths, targetMonth),
    }))
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
    storageType: ProductSearchConditions['storageTypes'][number],
  ) => {
    setSearchConditions((prev) => ({
      ...prev,
      storageTypes: toggleSelectedValue(prev.storageTypes, storageType),
    }))
  }

  const handleMainIngredientRegionToggle = (mainIngredientRegion: number) => {
    setSearchConditions((prev) => ({
      ...prev,
      mainIngredientRegionIds: toggleSelectedValue(
        prev.mainIngredientRegionIds,
        mainIngredientRegion,
      ),
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
      const result = await getProductSearchData(searchConditions, 1, PAGE_SIZE)
      setSearchResult(result)
      setAppliedSearchConditions(searchConditions)

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
      const result = await getProductSearchData(
        appliedSearchConditions,
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
          提供可能時期
        </h3>
        <div className="grid grid-cols-2 gap-x-2 gap-y-3">
          {targetMonths.map((targetMonth) => {
            const isSelected =
              searchConditions.targetMonths.includes(targetMonth)

            return (
              <button
                key={targetMonth}
                type="button"
                aria-pressed={isSelected}
                onClick={() => handleTargetMonthToggle(targetMonth)}
                className={
                  isSelected
                    ? 'rounded-full border border-border bg-border px-2 py-1 text-bg'
                    : 'rounded-full border border-border bg-bg px-2 py-1 hover:bg-textbg/40'
                }
              >
                {formatTargetMonth(targetMonth)}
              </button>
            )
          })}
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

      <div className="pb-4">
        <h3 className="mb-2 border-b-2 border-border px-2 text-left font-bold text-border textaccent">
          主原料産地
        </h3>
        <div className="grid grid-cols-3 justify-items-start gap-3">
          {[...searchOptions.mainIngredientOriginRegions]
            .sort((a, b) => a.id - b.id)
            .map((region) => (
              <label
                key={region.id}
                className="flex cursor-pointer items-center gap-1 rounded-md px-1 py-1.5 hover:bg-textbg/40"
              >
                <input
                  type="checkbox"
                  className="accent-border"
                  value={region.id}
                  checked={searchConditions.mainIngredientRegionIds.includes(
                    region.id,
                  )}
                  onChange={() => handleMainIngredientRegionToggle(region.id)}
                />
                <span>{region.name}</span>
              </label>
            ))}
        </div>
      </div>
    </>
  )

  return (
    <>
      <div className="mx-auto flex max-w-300 flex-col px-4 text-center md:px-6 lg:flex-row lg:gap-8 lg:px-8 lg:py-6">
        <SearchFilterPanel
          title="商品検索"
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
              件の商品が見つかりました
            </p>
          </div>
          <div>
            {searchError && (
              <p role="alert" className="mb-4 text-left text-error">
                {searchError}
              </p>
            )}
            {searchResult.products.length === 0 ? (
              <div
                role="status"
                className="rounded-xl bg-textbg/30 px-6 py-12 text-center"
              >
                <p className="mb-4 text-lg">
                  条件に一致する商品が見つかりませんでした。
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
                {searchResult.products.map((product, index) => (
                  <div key={product.id}>
                    <Link
                      to={`/products/${product.id}`}
                      className="group block rounded-2xl focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-border"
                    >
                      <article className="md:p-6 p-4 rounded-2xl shadow-[0_1px_2px_0.5px_rgb(0_0_0/0.30)] transition duration-300 group-hover:-translate-y-0.5 group-hover:shadow-lg">
                        <div className="flex flex-col pb-4 gap-4 md:flex-row">
                          <div className="aspect-4/3 w-full shrink-0 overflow-hidden rounded-xl md:w-[30%]">
                            <img
                              src={product.mainImageUrl}
                              alt={`${product.name}の商品画像`}
                              className="h-full w-full object-cover transition-transform duration-300 group-hover:scale-105"
                            />
                          </div>
                          <div className="min-w-0 flex-1">
                            <p className="text-left">{product.businessName}</p>
                            <h3 className="text-2xl text-left mb-2">
                              {product.name}
                            </h3>
                            <div className="flex flex-wrap gap-2">
                              <span className="px-3 py-1 rounded-full bg-badgearea">
                                {product.mainIngredientOriginPrefectureName}
                              </span>
                              <span className="px-3 py-1 rounded-full bg-badgesto">
                                {product.storageTypeLabel}
                              </span>
                              <span className="px-3 py-1 rounded-full bg-badgecate">
                                {product.productCategoryName}
                              </span>
                            </div>
                          </div>
                        </div>
                        <div className="overflow-hidden border border-border">
                          {/* スマホ・タブレット用の見出し */}
                          <div className="grid grid-cols-[8rem_minmax(0,1fr)] border-b border-border bg-textbg md:hidden">
                            <div className="border-r border-border px-3 py-2 text-left">
                              提供可能月
                            </div>
                            <div className="px-3 py-2 text-center">
                              提供可能数量
                            </div>
                          </div>

                          <div className="md:grid md:grid-cols-[9rem_repeat(6,minmax(0,1fr))]">
                            {/* PC用の左側の項目名 */}
                            <div className="hidden bg-textbg md:grid md:grid-rows-2">
                              <div className="flex items-center border-b border-border px-3 py-2 text-left text-sm">
                                提供可能月
                              </div>
                              <div className="flex items-center px-3 py-2 text-left text-sm">
                                提供可能数量
                              </div>
                            </div>

                            {product.monthlySupplyCapacities.map((capacity) => {
                              return (
                                <div
                                  key={capacity.targetMonth}
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
                                    {formatTargetMonth(capacity.targetMonth)}
                                  </div>

                                  <div className="flex justify-center items-center px-2 py-1 text-sm bg-bg">
                                    {capacity.availableQuantity}
                                  </div>
                                </div>
                              )
                            })}
                          </div>
                        </div>
                      </article>
                    </Link>
                    {(index + 1) % 5 === 0 &&
                      index !== searchResult.products.length - 1 && (
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
      {searchResult.products.length > 0 && (
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
        fixed right-4 bottom-[calc(env(safe-area-inset-bottom)+2.5rem)]
        z-40 rounded-full bg-border px-5 py-3
        font-bold text-bg shadow-lg
        lg:hidden
        "
      >
        検索条件を変更
      </button>

      <MobileSearchFilterDialog
        title="商品"
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

export default ProductSearchPage
