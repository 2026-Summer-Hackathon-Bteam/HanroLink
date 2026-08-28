import { useState, useEffect, type SubmitEvent } from 'react'
import FormRow from '../../../components/FormRow'
import type {
  ProcurementRequestFormData,
  MonthlyProcurementQuantityFormData,
  ProcurementRequestFormOptions,
} from '../procurementRequestFormTypes'
import { formatTargetMonth } from '../../../shared/utils/yearMonth'
import { getProcurementRequestFormOptions } from '../procurementRequestFormService'
import type { ProcurementRequestFormProps } from '../procurementRequestFormTypes'

function ProcurementRequestForm({
  mode,
  initialValues,
  onSubmit,
  onCancel,
}: ProcurementRequestFormProps) {
  const [procurementRequestInformations, setProcurementRequestInformations] =
    useState<ProcurementRequestFormData>(
      initialValues.procurementRequestInformations,
    )
  const [monthlyProcurementQuantities, setMonthlyProcurementQuantities] =
    useState<MonthlyProcurementQuantityFormData[]>(
      initialValues.monthlyProcurementQuantities,
    )
  const [procurementRequestFormOptions, setProcurementRequestFormOptions] =
    useState<ProcurementRequestFormOptions | null>(null)
  const [selectedProductCategoryGroupId, setSelectedProductCategoryGroupId] =
    useState<number | ''>('')
  const [formOptionError, setFormOptionError] = useState('')
  const [formError, setFormError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    let isCancelled = false

    const loadProcurementRequestFormOptions = async () => {
      try {
        const result = await getProcurementRequestFormOptions()

        if (!isCancelled) {
          setProcurementRequestFormOptions(result)

          const selectedCategory = result.productCategories.find(
            (category) =>
              category.id ===
              initialValues.procurementRequestInformations.productCategoryId,
          )

          setSelectedProductCategoryGroupId(
            selectedCategory?.productCategoryGroupId ?? '',
          )
        }
      } catch (error: unknown) {
        if (!isCancelled) {
          setFormOptionError(
            error instanceof Error
              ? error.message
              : 'フォーム選択肢の取得に失敗しました。',
          )
        }
      }
    }

    void loadProcurementRequestFormOptions()

    return () => {
      isCancelled = true
    }
  }, [initialValues.procurementRequestInformations.productCategoryId])

  const monthlyProcurementQuantityChange = (
    targetMonth: string,
    value: string,
  ) => {
    setMonthlyProcurementQuantities((prev) =>
      prev.map((quantity) =>
        quantity.targetMonth === targetMonth
          ? {
              ...quantity,
              desiredQuantity: value === '' ? '' : Number(value),
            }
          : quantity,
      ),
    )
  }

  const handleSubmit = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault()

    if (isSubmitting) return

    if (procurementRequestInformations.storageTypes.length === 0) {
      // 保存方法のエラーを表示
      setFormError('保存方法は１つ以上選択してください。')
      return
    }

    setIsSubmitting(true)
    setFormError('')

    try {
      await onSubmit({
        procurementRequestInformations,
        monthlyProcurementQuantities,
      })
    } catch (error: unknown) {
      setFormError(
        error instanceof Error ? error.message : '募集情報の送信に失敗しました',
      )
    } finally {
      setIsSubmitting(false)
    }
  }

  if (formOptionError) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {formOptionError}
      </p>
    )
  }

  if (!procurementRequestFormOptions) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  return (
    <form className="flex flex-col mx-auto" onSubmit={handleSubmit}>
      <h3 className="text-start pl-1">募集情報</h3>
      <div className="overflow-hidden border border-border divide-y divide-border mb-8">
        <FormRow label="タイトル" htmlFor="title">
          <input
            id="title"
            name="title"
            type="text"
            className="w-full"
            value={procurementRequestInformations.title}
            onChange={(e) => {
              setProcurementRequestInformations((prev) => ({
                ...prev,
                title: e.target.value,
              }))
            }}
            required
            maxLength={255}
          />
        </FormRow>

        <FormRow label="説明文" htmlFor="description">
          <textarea
            id="description"
            name="description"
            className="w-full h-30 p-2"
            value={procurementRequestInformations.description}
            onChange={(e) =>
              setProcurementRequestInformations((prev) => ({
                ...prev,
                description: e.target.value,
              }))
            }
            required
            maxLength={1000}
          />
        </FormRow>

        <FormRow label="商品カテゴリー" htmlFor="productCategoryGroupId">
          <div className="flex flex-col gap-2 w-full min-w-0">
            <div className="flex flex-col items-start md:flex-row md:items-center md:gap-2">
              <label
                htmlFor="productCategoryGroupId"
                className="shrink-0 whitespace-nowrap"
              >
                カテゴリーグループ：
              </label>
              <select
                id="productCategoryGroupId"
                name="productCategoryGroupId"
                value={selectedProductCategoryGroupId}
                onChange={(e) => {
                  const value = e.target.value
                  setSelectedProductCategoryGroupId(
                    value === '' ? '' : Number(value),
                  )
                  setProcurementRequestInformations((prev) => ({
                    ...prev,
                    productCategoryId: '',
                  }))
                }}
                required
                className="h-11 w-full rounded-lg border-[0.5px] border-text px-3 shadow-sm md:w-80"
              >
                <option value="">選択してください</option>
                {[...procurementRequestFormOptions.productCategoryGroups]
                  .sort((a, b) => a.id - b.id)
                  .map((option) => (
                    <option key={option.id} value={option.id}>
                      {option.name}
                    </option>
                  ))}
              </select>
            </div>
            <div className="flex flex-col items-start md:flex-row md:items-center md:gap-2">
              <label
                htmlFor="productCategoryId"
                className="shrink-0 whitespace-nowrap"
              >
                商品カテゴリー：
              </label>
              <select
                id="productCategoryId"
                name="productCategoryId"
                value={procurementRequestInformations.productCategoryId}
                onChange={(e) => {
                  const value = e.target.value
                  setProcurementRequestInformations((prev) => ({
                    ...prev,
                    productCategoryId: value === '' ? '' : Number(value),
                  }))
                }}
                disabled={selectedProductCategoryGroupId === ''}
                required
                className="h-11 w-full rounded-lg border-[0.5px] border-text px-3 shadow-sm md:w-80 disabled:cursor-not-allowed disabled:opacity-50"
              >
                <option value="">
                  {selectedProductCategoryGroupId === ''
                    ? '先にカテゴリーグループを選択してください'
                    : '選択してください'}
                </option>
                {[...procurementRequestFormOptions.productCategories]
                  .filter(
                    (option) =>
                      option.productCategoryGroupId ===
                      selectedProductCategoryGroupId,
                  )
                  .sort((a, b) => a.id - b.id)
                  .map((option) => (
                    <option key={option.id} value={option.id}>
                      {option.name}
                    </option>
                  ))}
              </select>
            </div>
          </div>
        </FormRow>

        <fieldset className="m-0 min-w-0 border-0 p-0 border-b border-border">
          <legend className="sr-only">保存方法</legend>

          <div className="grid md:grid-cols-[16rem_1fr]">
            <div
              aria-hidden="true"
              className="flex items-center bg-textbg px-5 py-4 text-left md:border-r md:border-border"
            >
              保存方法
            </div>

            <div className="flex flex-wrap items-center gap-4 p-3">
              {procurementRequestFormOptions.storageTypes.map((type) => (
                <label key={type.value} className="flex items-center gap-1">
                  <input
                    type="checkbox"
                    name="storageTypes"
                    value={type.value}
                    checked={procurementRequestInformations.storageTypes.includes(
                      type.value,
                    )}
                    onChange={(event) => {
                      setFormError('')
                      const checked = event.target.checked

                      setProcurementRequestInformations((previous) => ({
                        ...previous,
                        storageTypes: checked
                          ? [...previous.storageTypes, type.value]
                          : previous.storageTypes.filter(
                              (storageType) => storageType !== type.value,
                            ),
                      }))
                    }}
                  />

                  <span>{type.label}</span>
                </label>
              ))}
            </div>
          </div>
        </fieldset>

        <FormRow label="希望単価（任意）" htmlFor="desiredUnitPrice">
          <div className="flex items-center gap-1">
            <input
              id="desiredUnitPrice"
              name="desiredUnitPrice"
              type="number"
              value={procurementRequestInformations.desiredUnitPrice}
              onChange={(e) => {
                const value = e.target.value
                setProcurementRequestInformations((prev) => ({
                  ...prev,
                  desiredUnitPrice: value === '' ? '' : Number(value),
                }))
              }}
              min={1}
            />
            <p>円</p>
          </div>
        </FormRow>

        <FormRow
          label="納品時の賞味期限残日数（任意）"
          htmlFor="deliveryShelfLifeDays"
        >
          <div className="flex items-center gap-1">
            <input
              id="deliveryShelfLifeDays"
              name="deliveryShelfLifeDays"
              type="number"
              value={procurementRequestInformations.deliveryShelfLifeDays}
              onChange={(e) => {
                const value = e.target.value
                setProcurementRequestInformations((prev) => ({
                  ...prev,
                  deliveryShelfLifeDays: value === '' ? '' : Number(value),
                }))
              }}
              min={1}
            />
            <p>日</p>
          </div>
        </FormRow>

        <FormRow label="必須の取引条件（任意）" htmlFor="requiredTradeTerms">
          <input
            id="requiredTradeTerms"
            name="requiredTradeTerms"
            type="text"
            className="w-full"
            value={procurementRequestInformations.requiredTradeTerms}
            onChange={(e) =>
              setProcurementRequestInformations((prev) => ({
                ...prev,
                requiredTradeTerms: e.target.value,
              }))
            }
            maxLength={1000}
          />
        </FormRow>
      </div>

      <h3 className="text-start pl-1">募集期間</h3>
      <div className="overflow-hidden border border-border">
        {/* スマホ・タブレット用の見出し */}
        <div className="grid grid-cols-[8rem_minmax(0,1fr)] border-b border-border bg-textbg lg:hidden">
          <div className="border-r border-border px-3 py-2 text-left">
            希望月
          </div>
          <div className="px-3 py-2 text-left">希望数量</div>
        </div>

        <div className="lg:grid lg:grid-cols-[16rem_repeat(6,minmax(0,1fr))]">
          {/* PC用の左側の項目名 */}
          <div className="hidden bg-textbg lg:grid lg:grid-rows-2">
            <div className="flex items-center border-b border-border px-5 py-4 text-left">
              希望月
            </div>
            <div className="flex items-center px-5 py-4 text-left">
              希望数量
            </div>
          </div>

          {monthlyProcurementQuantities.map((quantity) => {
            const inputId = `desiredQuantity-${quantity.targetMonth}`

            return (
              <div
                key={quantity.targetMonth}
                className="
                    grid grid-cols-[8rem_minmax(0,1fr)]
                    border-b border-border last:border-b-0
                    lg:grid-cols-1 lg:grid-rows-2
                    lg:border-b-0 lg:border-l
                  "
              >
                <label
                  htmlFor={inputId}
                  className="
                      flex items-center bg-textbg px-3 py-3 text-left
                      border-r border-border
                      lg:justify-center lg:bg-bg
                      lg:border-r-0 lg:border-b
                    "
                >
                  {formatTargetMonth(quantity.targetMonth)}
                </label>

                <div className="flex items-center p-3">
                  <input
                    id={inputId}
                    name={inputId}
                    type="number"
                    min={0}
                    required
                    value={quantity.desiredQuantity}
                    onChange={(event) => {
                      monthlyProcurementQuantityChange(
                        quantity.targetMonth,
                        event.target.value,
                      )
                    }}
                    className="w-full text-left"
                    aria-label={`${formatTargetMonth(quantity.targetMonth)}の希望数量`}
                  />
                </div>
              </div>
            )
          })}
        </div>
      </div>

      <div className="mt-16 flex justify-center gap-4">
        {mode === 'edit' && onCancel && (
          <button
            type="button"
            onClick={onCancel}
            className="flex h-9 w-45 items-center justify-center rounded-full button-base button-secondary"
            disabled={isSubmitting}
          >
            キャンセル
          </button>
        )}
        <button
          type="submit"
          className="h-9 w-45 rounded-full button-base button-form"
          disabled={isSubmitting}
        >
          {isSubmitting ? mode=== 'create'? '登録中...': '更新中...' : mode === 'create' ? '登録する' : '更新する'}
        </button>
      </div>
      {formError && (
        <p role="alert" className="whitespace-pre-line text-center mt-2 text-error">
          {formError}
        </p>
      )}
    </form>
  )
}

export default ProcurementRequestForm
