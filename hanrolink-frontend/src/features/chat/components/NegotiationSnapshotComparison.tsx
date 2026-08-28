import type { ChatNegotiationSnapshot } from '../ChatTypes'
import SnapshotComparisonRow from './SnapshotComparisonRow'

type NegotiationSnapshotComparisonProps = {
  snapshot: ChatNegotiationSnapshot
}

type ProductChangedField =
  ChatNegotiationSnapshot['productChangedFields'][number]

type ProcurementRequestChangedField = NonNullable<
  ChatNegotiationSnapshot['procurementRequestChangedFields']
>[number]

type ProductSnapshot = ChatNegotiationSnapshot['requestedProductSnapshot']

type ProductValueGetter = (snapshot: ProductSnapshot) => string

type ProcurementRequestSnapshot = NonNullable<
  ChatNegotiationSnapshot['requestedProcurementRequestSnapshot']
>

type ProcurementRequestValueFormatter = (
  snapshot: ProcurementRequestSnapshot,
) => string

const productChangedFieldLabels = {
  PRODUCT_CATEGORY: '商品カテゴリー',
  MAIN_INGREDIENT_ORIGIN_PREFECTURE: '主原料産地',
  NAME: '商品名',
  CONTENT_QUANTITY: '内容量',
  EXPIRATION_TYPE: '賞味期限／消費期限',
  SHELF_LIFE_DAYS: '期限日数',
  STORAGE_TYPE: '保存方法',
  DESIRED_RETAIL_PRICE: '希望小売価格',
  ALLERGY_INFORMATION: 'アレルギー表示',
  CERTIFICATION_INFORMATION: '認証等',
  CASE_SIZE: 'ケースサイズ',
  UNITS_PER_CASE: '1ケース当たり入数',
  MINIMUM_ORDER_QUANTITY: '最低発注数量',
  SHIPPING_LEAD_TIME_DAYS: '発注リードタイム',
  SALES_AREA_RESTRICTION: '販売地域制限',
  MONTHLY_SUPPLY_CAPACITIES: '提供可能月・数量',
  PRODUCT_STORIES: '商品ストーリー',
} satisfies Record<ProductChangedField, string>

const procurementRequestChangedFieldLabels = {
  PRODUCT_CATEGORY: '商品カテゴリー',
  TITLE: '募集情報名',
  DESCRIPTION: '募集内容',
  REQUIRED_TRADE_TERMS: '希望取引条件',
  DESIRED_UNIT_PRICE: '希望単価',
  DELIVERY_SHELF_LIFE_DAYS: '納品時の期限残日数',
  STORAGE_TYPES: '保存方法',
  MONTHLY_PROCUREMENT_QUANTITIES: '希望月・希望数量',
} satisfies Record<ProcurementRequestChangedField, string>

const formatOptionalText = (value: string | null | undefined): string =>
  value && value.trim() ? value : '-'

const formatOptionalNumber = (
  value: number | null | undefined,
  suffix = '',
): string => (value === null || value === undefined ? '-' : `${value.toLocaleString()}${suffix}`)

const formatTargetMonth = (targetMonth: string): string => {
  const [year, month] = targetMonth.split('-')

  return `${year}年${Number(month)}月`
}

const productValueFormatters = {
  PRODUCT_CATEGORY: (snapshot) => snapshot.productCategoryName,

  MAIN_INGREDIENT_ORIGIN_PREFECTURE: (snapshot) =>
    snapshot.mainIngredientOriginPrefectureName,

  NAME: (snapshot) => snapshot.name,

  CONTENT_QUANTITY: (snapshot) => snapshot.contentQuantity,

  EXPIRATION_TYPE: (snapshot) => snapshot.expirationTypeName,

  SHELF_LIFE_DAYS: (snapshot) =>
    formatOptionalNumber(snapshot.shelfLifeDays, '日'),

  STORAGE_TYPE: (snapshot) => snapshot.storageTypeName,

  DESIRED_RETAIL_PRICE: (snapshot) =>
    `${snapshot.desiredRetailPrice.toLocaleString()}円`,

  ALLERGY_INFORMATION: (snapshot) =>
    formatOptionalText(snapshot.allergyInformation),

  CERTIFICATION_INFORMATION: (snapshot) =>
    formatOptionalText(snapshot.certificationInformation),

  CASE_SIZE: (snapshot) => formatOptionalText(snapshot.caseSize),

  UNITS_PER_CASE: (snapshot) => formatOptionalNumber(snapshot.unitsPerCase),

  MINIMUM_ORDER_QUANTITY: (snapshot) =>
    formatOptionalNumber(snapshot.minimumOrderQuantity),

  SHIPPING_LEAD_TIME_DAYS: (snapshot) =>
    formatOptionalNumber(snapshot.shippingLeadTimeDays, '日'),

  SALES_AREA_RESTRICTION: (snapshot) =>
    formatOptionalText(snapshot.salesAreaRestriction),

  MONTHLY_SUPPLY_CAPACITIES: (snapshot) =>
    snapshot.monthlySupplyCapacities
      .map(
        (capacity) =>
          `${formatTargetMonth(capacity.targetMonth)}：${capacity.availableQuantity.toLocaleString()}`,
      )
      .join('\n'),

  PRODUCT_STORIES: (snapshot) =>
    snapshot.productStories
      .map(
        (story, index) => `${index + 1}. ${story.sectionTitle}\n${story.body}`,
      )
      .join('\n\n'),
} satisfies Record<ProductChangedField, ProductValueGetter>

const productFields = Object.keys(
  productChangedFieldLabels,
) as ProductChangedField[]

const procurementRequestValueFormatters = {
  PRODUCT_CATEGORY: (snapshot) => snapshot.productCategoryName,

  TITLE: (snapshot) => snapshot.title,

  DESCRIPTION: (snapshot) => snapshot.description,

  REQUIRED_TRADE_TERMS: (snapshot) =>
    formatOptionalText(snapshot.requiredTradeTerms),

  DESIRED_UNIT_PRICE: (snapshot) =>
    formatOptionalNumber(snapshot.desiredUnitPrice, '円'),

  DELIVERY_SHELF_LIFE_DAYS: (snapshot) =>
    formatOptionalNumber(snapshot.deliveryShelfLifeDays, '日'),

  STORAGE_TYPES: (snapshot) =>
    snapshot.storageTypeNames.length > 0
      ? snapshot.storageTypeNames.join('、')
      : '-',

  MONTHLY_PROCUREMENT_QUANTITIES: (snapshot) =>
    snapshot.monthlyProcurementQuantities
      .map(
        (quantity) =>
          `${formatTargetMonth(quantity.targetMonth)}：${quantity.desiredQuantity.toLocaleString()}`,
      )
      .join('\n'),
} satisfies Record<
  ProcurementRequestChangedField,
  ProcurementRequestValueFormatter
>

const procurementRequestFields = Object.keys(
  procurementRequestChangedFieldLabels,
) as ProcurementRequestChangedField[]

function NegotiationSnapshotComparison({
  snapshot,
}: NegotiationSnapshotComparisonProps) {
  const procurementRequestChangedFields =
    snapshot.procurementRequestChangedFields ?? []

  const requestedProcurementRequestSnapshot =
    snapshot.requestedProcurementRequestSnapshot

  const acceptedProcurementRequestSnapshot =
    snapshot.acceptedProcurementRequestSnapshot

  const changedFieldCount =
    snapshot.productChangedFields.length +
    procurementRequestChangedFields.length

  return (
    <details className="rounded-lg bg-bg p-3 text-left shadow-sm ring-1 ring-text/10">
      <summary className="cursor-pointer font-medium text-accent">
        {changedFieldCount > 0
          ? `商談開始までに${changedFieldCount}項目が変更されています`
          : '商談希望送信時から条件の変更はありません'}
      </summary>

      <div className="mt-3 space-y-3 text-sm">
        <div>
          <p className="font-medium">商品情報</p>

          <div className="mt-1 overflow-hidden border border-border/30">
            <div
              className="hidden bg-textbg/30 font-medium md:border-b md:border-border/30
                md:grid md:grid-cols-[12rem_minmax(0,1fr)_minmax(0,1fr)]"
            >
              <span className="border-r border-border/30 px-3 py-2">項目</span>

              <span className="px-3 py-2">商談希望送信時</span>

              <span className="border-l border-border/30 px-3 py-2">
                商談開始時
              </span>
            </div>

            <dl className="divide-y divide-border/30">
              {productFields.map((field) => {
                const formatValue = productValueFormatters[field]

                return (
                  <SnapshotComparisonRow
                    key={field}
                    label={productChangedFieldLabels[field]}
                    requestedValue={formatValue(
                      snapshot.requestedProductSnapshot,
                    )}
                    acceptedValue={formatValue(
                      snapshot.acceptedProductSnapshot,
                    )}
                    changed={snapshot.productChangedFields.includes(field)}
                  />
                )
              })}
            </dl>
          </div>
        </div>

        {snapshot.negotiationTargetType === 'PROCUREMENT_REQUEST' &&
          requestedProcurementRequestSnapshot &&
          acceptedProcurementRequestSnapshot && (
            <div>
              <p className="font-medium">募集情報</p>

              <div className="mt-1 overflow-hidden border border-border/30">
                <div
                  className="hidden bg-textbg/30 font-medium md:border-b md:border-border/30
            md:grid md:grid-cols-[12rem_minmax(0,1fr)_minmax(0,1fr)]"
                >
                  <span className="border-r border-border/30 px-3 py-2">
                    項目
                  </span>

                  <span className="px-3 py-2">商談希望送信時</span>

                  <span className="border-l border-border/30 px-3 py-2">
                    商談開始時
                  </span>
                </div>

                <dl className="divide-y divide-border/30">
                  {procurementRequestFields.map((field) => {
                    const formatValue = procurementRequestValueFormatters[field]

                    return (
                      <SnapshotComparisonRow
                        key={field}
                        label={procurementRequestChangedFieldLabels[field]}
                        requestedValue={formatValue(
                          requestedProcurementRequestSnapshot,
                        )}
                        acceptedValue={formatValue(
                          acceptedProcurementRequestSnapshot,
                        )}
                        changed={procurementRequestChangedFields.includes(
                          field,
                        )}
                      />
                    )
                  })}
                </dl>
              </div>
            </div>
          )}

        {changedFieldCount === 0 && (
          <p>
            {snapshot.negotiationTargetType === 'PRODUCT'
              ? '商談希望送信時と商談開始時の商品条件は同じです。'
              : '商談希望送信時と商談開始時の商品・募集条件は同じです。'}
          </p>
        )}
      </div>
    </details>
  )
}

export default NegotiationSnapshotComparison
