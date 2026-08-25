import { useState, useEffect, useRef } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import type { ProcurementRequestDetailData } from '../features/procurementRequest/procurementRequestDetailTypes'
import {
  deleteProcurementRequest,
  getProcurementRequestDetailData,
} from '../features/procurementRequest/procurementRequestDetailService'
import DataRow from '../components/DataRow'
import { formatTargetMonth } from '../shared/utils/yearMonth'
import type { NegotiationSelectableProduct } from '../features/negotiation/negotiationRequestTypes'
import {
  createProcurementNegotiationRequest,
  getNegotiationSelectableProducts,
} from '../features/negotiation/negotiationRequestService'

function ProcurementRequestDetailPage() {
  const [procurementRequestDetailData, setProcurementRequestDetailData] =
    useState<ProcurementRequestDetailData | null>(null)
  const [error, setError] = useState('')
  const deleteDialogRef = useRef<HTMLDialogElement>(null)
  const navigate = useNavigate()
  const [isDeleting, setIsDeleting] = useState(false)
  const [deleteError, setDeleteError] = useState('')
  const { procurementRequestId } = useParams()
  const negotiationDialogRef = useRef<HTMLDialogElement>(null)
  const [selectableProducts, setSelectableProducts] = useState<
    NegotiationSelectableProduct[]
  >([])
  const [selectedProductId, setSelectedProductId] = useState('')
  const [isLoadingSelectableProducts, setIsLoadingSelectableProducts] =
    useState(false)
  const [isSubmittingNegotiation, setIsSubmittingNegotiation] = useState(false)
  const [negotiationError, setNegotiationError] = useState('')
  const [negotiationSucceeded, setNegotiationSucceeded] = useState(false)

  useEffect(() => {
    if (procurementRequestId === undefined) return

    let isCancelled = false

    const loadProcurementRequestData = async () => {
      try {
        const result =
          await getProcurementRequestDetailData(procurementRequestId)

        if (!isCancelled) {
          setProcurementRequestDetailData(result)
        }
      } catch {
        if (!isCancelled) {
          setError('募集情報データの取得に失敗しました。')
        }
      }
    }
    void loadProcurementRequestData()

    return () => {
      isCancelled = true
    }
  }, [procurementRequestId])

  const handleDelete = async () => {
    if (isDeleting || !procurementRequestDetailData) return
    if (!procurementRequestId) return

    setIsDeleting(true)
    setDeleteError('')

    try {
      await deleteProcurementRequest(procurementRequestId)
      deleteDialogRef.current?.close()
      navigate('/mypage/buyer')
    } catch (error) {
      setDeleteError(
        error instanceof Error
          ? error.message
          : '募集情報の削除に失敗しました。',
      )
      setIsDeleting(false)
    }
  }

  const handleOpenNegotiationDialog = async () => {
    if (
      !procurementRequestDetailData ||
      !procurementRequestDetailData.permissions.canCreateNegotiationRequest ||
      procurementRequestDetailData.hasMyActiveNegotiationRequest ||
      isLoadingSelectableProducts
    ) {
      return
    }

    const dialog = negotiationDialogRef.current
    if (!dialog) return

    setSelectableProducts([])
    setSelectedProductId('')
    setNegotiationError('')
    setIsLoadingSelectableProducts(true)
    setNegotiationSucceeded(false)
    dialog.showModal()

    try {
      const products = await getNegotiationSelectableProducts()
      setSelectableProducts(products)
    } catch (error: unknown) {
      setNegotiationError(
        error instanceof Error
          ? error.message
          : '選択可能な商品の取得に失敗しました。',
      )
    } finally {
      setIsLoadingSelectableProducts(false)
    }
  }

  const handleCreateNegotiationRequest = async () => {
    if (
      isSubmittingNegotiation ||
      isLoadingSelectableProducts ||
      !procurementRequestDetailData ||
      !procurementRequestId ||
      !selectedProductId ||
      !procurementRequestDetailData.permissions.canCreateNegotiationRequest ||
      procurementRequestDetailData.hasMyActiveNegotiationRequest
    ) {
      return
    }

    setIsSubmittingNegotiation(true)
    setNegotiationError('')

    try {
      await createProcurementNegotiationRequest(
        procurementRequestId,
        selectedProductId,
      )

      setProcurementRequestDetailData((current) =>
        current ? { ...current, hasMyActiveNegotiationRequest: true } : current,
      )

      setNegotiationSucceeded(true)
    } catch (error: unknown) {
      setNegotiationError(
        error instanceof Error
          ? error.message
          : '商談希望の送信に失敗しました。',
      )
    } finally {
      setIsSubmittingNegotiation(false)
    }
  }

  if (error) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {error}
      </p>
    )
  }

  if (!procurementRequestId) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        募集情報IDが正しくありません。
      </p>
    )
  }

  if (!procurementRequestDetailData) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  return (
    <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
      {procurementRequestDetailData.permissions.canManage && (
        <section
          aria-labelledby="procurement-request-management-title"
          className="mt-6 border-y border-dashed border-border bg-textbg/30 px-4 py-4"
        >
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <h3
              id="procurement-request-management-title"
              className="text-left text-base font-bold"
            >
              募集情報管理
            </h3>

            <div className="flex flex-wrap gap-3">
              <Link
                to={`/procurement-requests/${procurementRequestId}/edit`}
                className="rounded-full button-base button-primary px-5 py-2 text-bg hover:bg-accent/80"
              >
                編集する
              </Link>

              <button
                type="button"
                className="rounded-full button-base button-danger-outline px-5 py-2 text-error"
                onClick={() => deleteDialogRef.current?.showModal()}
              >
                削除する
              </button>
            </div>
          </div>
        </section>
      )}

      <div className="mb-12">
        <h2 className="mb-0!">{procurementRequestDetailData.title}</h2>
        <Link
          to={`/buyer/${procurementRequestDetailData.buyer.businessId}`}
          className="text-other underline underline-offset-2 hover:no-underline"
        >
          {procurementRequestDetailData.buyer.businessName}
        </Link>
      </div>

      {/* 説明文 */}
      <section className="mb-12">
        <p>{procurementRequestDetailData.description}</p>
      </section>

      {/* 募集情報項目 */}
      <section className="mb-8">
        <h3 className="mb-1 text-left">募集情報</h3>
        <div className="grid grid-cols-1 lg:gap-5 lg:grid-cols-2 lg:items-start">
          <dl className="overflow-hidden border border-border divide-y divide-border">
            <DataRow itemName="商品カテゴリー">
              {procurementRequestDetailData.productCategory.name}
            </DataRow>

            <DataRow itemName="保存方法">
              {procurementRequestDetailData.storageTypes
                .map((type) => type.label)
                .join('、')}
            </DataRow>

            <DataRow itemName="希望単価">
              {procurementRequestDetailData.desiredUnitPrice !== undefined
                ? `${procurementRequestDetailData.desiredUnitPrice}円`
                : '-'}
            </DataRow>
          </dl>

          <dl className="overflow-hidden border border-t-0 border-border divide-y divide-border lg:border-t">
            <DataRow itemName="納品時の賞味期限残日数">
              {procurementRequestDetailData.deliveryShelfLifeDays !== undefined
                ? `${procurementRequestDetailData.deliveryShelfLifeDays}日`
                : '-'}
            </DataRow>

            <DataRow itemName="必須の取引条件">
              {procurementRequestDetailData.requiredTradeTerms
                ? procurementRequestDetailData.requiredTradeTerms
                : '-'}
            </DataRow>
          </dl>
        </div>
      </section>

      {/* 希望月 */}
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

          {procurementRequestDetailData.monthlyProcurementQuantities.map(
            (quantity) => {
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
                  <div
                    className="
                            flex items-center bg-textbg px-3 py-3 text-left
                            border-r border-border
                            lg:justify-center lg:bg-bg
                            lg:border-r-0 lg:border-b
                          "
                  >
                    {formatTargetMonth(quantity.targetMonth)}
                  </div>

                  <div className="flex justify-center items-center p-3">
                    <p>{quantity.desiredQuantity}</p>
                  </div>
                </div>
              )
            },
          )}
        </div>
      </div>
      <div>
        <button
          type="button"
          className="h-9 w-45 block mx-auto mt-8 rounded-full button-base button-primary textaccent"
          disabled={
            procurementRequestDetailData.hasMyActiveNegotiationRequest ||
            !procurementRequestDetailData.permissions
              .canCreateNegotiationRequest
          }
          onClick={() => void handleOpenNegotiationDialog()}
        >
          商談希望を送る
        </button>
        {procurementRequestDetailData.hasMyActiveNegotiationRequest ? (
          <p className="pt-2">この募集情報には商談希望を送信済みです。</p>
        ) : !procurementRequestDetailData.permissions
            .canCreateNegotiationRequest ? (
          <p className="pt-2">
            バイヤーは募集情報に商談希望を送ることはできません。
          </p>
        ) : null}
      </div>

      <button
        type="button"
        onClick={() => navigate(-1)}
        className="mx-auto mt-16 h-9 w-45 rounded-full button-base button-form"
      >
        前のページに戻る
      </button>

      {/* 商談希望確認モーダル */}
      <dialog
        ref={negotiationDialogRef}
        aria-labelledby="create-negotiation-request-title"
        aria-describedby="create-negotiation-request-description"
        className="m-auto w-[min(90vw,40rem)] rounded-lg border-0 bg-bg p-6 shadow-xl backdrop:bg-black/50"
      >
        {negotiationSucceeded ? (
          // 送信成功時
          <>
            <h3
              id="create-negotiation-request-title"
              className="text-lg font-bold"
            >
              商談希望を送信しました
            </h3>

            <p
              id="create-negotiation-request-description"
              role="status"
              className="mt-4"
            >
              {procurementRequestDetailData.buyer.businessName}の「
              {procurementRequestDetailData.title}」への商談希望を送信しました。
            </p>

            <div className="mt-6 flex justify-center">
              <button
                type="button"
                className="rounded-full button-base button-primary px-5 py-2 text-bg"
                onClick={() => negotiationDialogRef.current?.close()}
              >
                閉じる
              </button>
            </div>
          </>
        ) : (
          // 送信前
          <>
            <h3
              id="create-negotiation-request-title"
              className="text-lg font-bold"
            >
              商談希望を送りますか？
            </h3>

            <p id="create-negotiation-request-description" className="mt-4">
              {procurementRequestDetailData.buyer.businessName}の「
              {procurementRequestDetailData.title}」に商談希望を送ります。
              提案する自社商品を1つ選択してください。
            </p>

            <ul className="mt-4 list-disc space-y-2 pl-5 text-left text-sm">
              <li>商談希望を送ると、相手のマイページに表示されます。</li>
              <li>
                相手が商談を開始すると、メッセージをやり取りするためのチャットが作成されます。
              </li>
              <li>
                商談希望の送信は、取引条件への同意または契約成立を意味するものではありません。
              </li>
            </ul>

            {isLoadingSelectableProducts ? (
              <p className="py-8 text-center">商品を読み込み中...</p>
            ) : selectableProducts.length > 0 ? (
              <fieldset
                className="mt-4 max-h-80 overflow-y-auto border-0"
                disabled={isSubmittingNegotiation}
              >
                <legend className="sr-only">提案する自社商品</legend>

                <ul className="space-y-3">
                  {selectableProducts.map((product) => (
                    <li key={product.id}>
                      <label
                        className={`flex cursor-pointer items-center gap-4 rounded-lg border p-3 ${
                          selectedProductId === product.id
                            ? 'border-accent bg-accentbg'
                            : 'border-border'
                        }`}
                      >
                        <input
                          type="radio"
                          name="negotiationProduct"
                          value={product.id}
                          checked={selectedProductId === product.id}
                          onChange={(event) =>
                            setSelectedProductId(event.target.value)
                          }
                        />

                        <img
                          src={product.mainImageUrl}
                          alt={`${product.name}のメイン画像`}
                          className="h-16 w-16 shrink-0 rounded object-cover"
                        />

                        <span className="min-w-0 text-left font-bold">
                          {product.name}
                        </span>
                      </label>
                    </li>
                  ))}
                </ul>
              </fieldset>
            ) : !negotiationError ? (
              <p className="py-8 text-center">
                商談希望に使用できる公開中の商品がありません。
                <br />
                商品を登録するか、非表示の商品を公開してください。
              </p>
            ) : null}

            {negotiationError && (
              <p role="alert" className="mt-4 text-center text-error">
                {negotiationError}
              </p>
            )}

            <div className="mt-6 flex justify-center gap-3">
              <button
                type="button"
                className="rounded-full button-base button-secondary px-5 py-2"
                onClick={() => negotiationDialogRef.current?.close()}
                disabled={isSubmittingNegotiation}
              >
                キャンセル
              </button>

              <button
                type="button"
                className="rounded-full button-base button-primary px-5 py-2 text-bg"
                onClick={() => void handleCreateNegotiationRequest()}
                disabled={
                  isLoadingSelectableProducts ||
                  isSubmittingNegotiation ||
                  !selectedProductId
                }
              >
                {isSubmittingNegotiation ? '送信中...' : '商談希望を送る'}
              </button>
            </div>
          </>
        )}
      </dialog>

      {/* 募集情報削除確認モーダル */}
      <dialog
        ref={deleteDialogRef}
        aria-labelledby="delete-procurement-request-title"
        className="m-auto w-[min(90vw,28rem)] rounded-lg border-0 bg-bg p-6 shadow-xl backdrop:bg-black/50"
      >
        <h3 id="delete-procurement-request-title" className="text-lg font-bold">
          募集情報を削除しますか？
        </h3>

        <p className="mt-4">
          「{procurementRequestDetailData.title}」を削除します。
        </p>

        <p className="mt-2 text-sm text-error">
          削除した募集情報は復元できません。
          <br />
          募集情報を削除すると、関連する商談希望も取り消されます。
          <br />
          この操作は元に戻せません。
        </p>

        <div className="mt-6 flex justify-center gap-3">
          <button
            type="button"
            onClick={() => deleteDialogRef.current?.close()}
            className="rounded-full button-base button-secondary px-5 py-2"
            disabled={isDeleting}
          >
            キャンセル
          </button>

          <button
            type="button"
            className="rounded-full px-5 py-2 button-base button-danger"
            onClick={handleDelete}
            disabled={isDeleting}
          >
            {isDeleting ? '削除中...' : '削除する'}
          </button>
        </div>
        {deleteError && (
          <p role="alert" className="mt-3 text-sm text-error">
            {deleteError}
          </p>
        )}
      </dialog>
    </div>
  )
}

export default ProcurementRequestDetailPage
