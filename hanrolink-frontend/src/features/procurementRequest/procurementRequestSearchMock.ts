import type {
  ProcurementRequestSearchOptions,
  ProcurementRequestSearchResult,
} from './procurementRequestSearchTypes'

export const procurementRequestSearchMock = {
  procurementRequests: [
    {
      id: 1,
      title: '国産りんごを使用したジャムを探しています',
      description:
        '自社店舗で販売するため、果肉感があり素材本来の風味をしっかり感じられる国産りんごのジャムを探しています。パンやヨーグルトに合わせやすく、幅広い年代のお客様に親しんでいただける味わいを希望します。原材料や製造方法にもこだわりがあり、継続的に安定した数量を供給できる商品を優先して検討します。内容量や納品条件、取引価格などの詳細については、商談の中で相談させてください。',
      productCategoryName: 'ジャム・はちみつ',
      storageTypeLabels: ['常温', '冷蔵'],
      monthlyProcurementQuantities: [
        { targetMonth: '2026-08', desiredQuantity: 100 },
        { targetMonth: '2026-09', desiredQuantity: 150 },
        { targetMonth: '2026-10', desiredQuantity: 200 },
        { targetMonth: '2026-11', desiredQuantity: 180 },
        { targetMonth: '2026-12', desiredQuantity: 250 },
        { targetMonth: '2027-01', desiredQuantity: 120 },
      ],
      buyer: {
        accountId: '550e8400-e29b-41d4-a716-446655440001',
        businessName: '株式会社ハンロマーケット',
      },
    },
    {
      id: 2,
      title: '北海道産素材を使った洋菓子を募集しています',
      description:
        '北海道産の乳製品や小麦を使用した、ギフト向けの洋菓子を探しています。',
      productCategoryName: '洋菓子',
      storageTypeLabels: ['冷蔵'],
      monthlyProcurementQuantities: [
        { targetMonth: '2026-08', desiredQuantity: 80 },
        { targetMonth: '2026-09', desiredQuantity: 100 },
        { targetMonth: '2026-10', desiredQuantity: 120 },
        { targetMonth: '2026-11', desiredQuantity: 150 },
        { targetMonth: '2026-12', desiredQuantity: 200 },
        { targetMonth: '2027-01', desiredQuantity: 100 },
      ],
      buyer: {
        accountId: '550e8400-e29b-41d4-a716-446655440002',
        businessName: '北日本セレクト株式会社',
      },
    },
    {
      id: 3,
      title: '店舗提供用の国産そばを探しています',
      description:
        '飲食店で提供するため、香りが強く安定して仕入れられる国産そばを希望します。',
      productCategoryName: '麺類',
      storageTypeLabels: ['冷蔵', '冷凍'],
      monthlyProcurementQuantities: [
        { targetMonth: '2026-08', desiredQuantity: 60 },
        { targetMonth: '2026-09', desiredQuantity: 80 },
        { targetMonth: '2026-10', desiredQuantity: 100 },
        { targetMonth: '2026-11', desiredQuantity: 100 },
        { targetMonth: '2026-12', desiredQuantity: 90 },
        { targetMonth: '2027-01', desiredQuantity: 70 },
      ],
      buyer: {
        accountId: '550e8400-e29b-41d4-a716-446655440003',
        businessName: '株式会社和食ダイニング',
      },
    },
    {
      id: 4,
      title: '愛媛県産みかんジュースを募集しています',
      description:
        '地域フェアで販売するため、果汁比率が高いみかんジュースを探しています。',
      productCategoryName: 'ソフトドリンク',
      storageTypeLabels: ['常温'],
      monthlyProcurementQuantities: [
        { targetMonth: '2026-08', desiredQuantity: 150 },
        { targetMonth: '2026-09', desiredQuantity: 150 },
        { targetMonth: '2026-10', desiredQuantity: 180 },
        { targetMonth: '2026-11', desiredQuantity: 200 },
        { targetMonth: '2026-12', desiredQuantity: 200 },
        { targetMonth: '2027-01', desiredQuantity: 160 },
      ],
      buyer: {
        accountId: '550e8400-e29b-41d4-a716-446655440004',
        businessName: '株式会社地域セレクション',
      },
    },
    {
      id: 5,
      title: '九州産さつまいもの和菓子を探しています',
      description:
        '秋冬向けの商品として、九州産さつまいもを使った和菓子を希望します。',
      productCategoryName: '和菓子',
      storageTypeLabels: ['常温', '冷蔵'],
      monthlyProcurementQuantities: [
        { targetMonth: '2026-08', desiredQuantity: 40 },
        { targetMonth: '2026-09', desiredQuantity: 60 },
        { targetMonth: '2026-10', desiredQuantity: 100 },
        { targetMonth: '2026-11', desiredQuantity: 140 },
        { targetMonth: '2026-12', desiredQuantity: 120 },
        { targetMonth: '2027-01', desiredQuantity: 80 },
      ],
      buyer: {
        accountId: '550e8400-e29b-41d4-a716-446655440005',
        businessName: '株式会社四季彩堂',
      },
    },
    {
      id: 6,
      title: '業務用の国産野菜を定期購入したいです',
      description:
        'レストラン各店舗で使用するため、安定供給が可能な国産野菜を募集しています。',
      productCategoryName: '野菜',
      storageTypeLabels: ['冷蔵'],
      monthlyProcurementQuantities: [
        { targetMonth: '2026-08', desiredQuantity: 300 },
        { targetMonth: '2026-09', desiredQuantity: 350 },
        { targetMonth: '2026-10', desiredQuantity: 400 },
        { targetMonth: '2026-11', desiredQuantity: 400 },
        { targetMonth: '2026-12', desiredQuantity: 350 },
        { targetMonth: '2027-01', desiredQuantity: 300 },
      ],
      buyer: {
        accountId: '550e8400-e29b-41d4-a716-446655440006',
        businessName: 'グリーンテーブル株式会社',
      },
    },
  ],

  pagination: {
    page: 1,
    pageSize: 6,
    totalCount: 8,
    totalPages: 2,
  },
} satisfies ProcurementRequestSearchResult

export const procurementRequestSearchOptionsMock = {
  productCategoryGroups: [
    {
      id: 1,
      name: '食品',
    },
    {
      id: 2,
      name: 'スイーツ・お菓子',
    },
    {
      id: 3,
      name: 'お酒',
    },
    {
      id: 4,
      name: '水・ソフトドリンク',
    },
    {
      id: 5,
      name: 'その他',
    },
  ],

  productCategories: [
    {
      id: 1,
      productCategoryGroupId: 1,
      name: '米・雑穀・シリアル',
    },
    {
      id: 2,
      productCategoryGroupId: 1,
      name: '麺類',
    },
    {
      id: 3,
      productCategoryGroupId: 1,
      name: '野菜',
    },
    {
      id: 7,
      productCategoryGroupId: 1,
      name: '果物',
    },
    {
      id: 11,
      productCategoryGroupId: 1,
      name: 'ジャム・はちみつ',
    },
    {
      id: 17,
      productCategoryGroupId: 2,
      name: '洋菓子',
    },
    {
      id: 18,
      productCategoryGroupId: 2,
      name: '和菓子',
    },
    {
      id: 23,
      productCategoryGroupId: 3,
      name: 'ビール・地ビール',
    },
    {
      id: 31,
      productCategoryGroupId: 4,
      name: 'ソフトドリンク',
    },
    {
      id: 38,
      productCategoryGroupId: 5,
      name: 'その他',
    },
  ],

  storageTypes: [
    {
      value: 'AMBIENT',
      label: '常温',
    },
    {
      value: 'REFRIGERATED',
      label: '冷蔵',
    },
    {
      value: 'FROZEN',
      label: '冷凍',
    },
  ],
} satisfies ProcurementRequestSearchOptions
