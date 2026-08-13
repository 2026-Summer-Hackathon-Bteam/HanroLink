import type { ProcurementRequestDetailData } from './procurementRequestDetailTypes'

export const procurementRequestDetailMock = {
  title: '国産りんごを使用したジャムを探しています',

  description:
    '自社店舗で販売するため、国産りんごを使用したジャムを探しています。果肉感があり、素材の風味を感じられる商品を希望します。',

  buyer: {
    accountId: '550e8400-e29b-41d4-a716-446655440000',
    businessName: '株式会社ハンロマーケット',
  },

  productCategory: {
    id: 11,
    name: 'ジャム・はちみつ',
  },

  requiredTradeTerms:
    '初回は少量での取引を希望します。納品方法や支払条件は商談時に相談させてください。',

  desiredUnitPrice: 650,

  deliveryShelfLifeDays: 90,

  storageTypes: [
    {
      value: 'AMBIENT',
      label: '常温',
    },
    {
      value: 'REFRIGERATED',
      label: '冷蔵',
    },
  ],

  monthlyProcurementQuantities: [
    {
      targetMonth: '2026-08',
      desiredQuantity: 100,
    },
    {
      targetMonth: '2026-09',
      desiredQuantity: 150,
    },
    {
      targetMonth: '2026-10',
      desiredQuantity: 200,
    },
    {
      targetMonth: '2026-11',
      desiredQuantity: 180,
    },
    {
      targetMonth: '2026-12',
      desiredQuantity: 250,
    },
    {
      targetMonth: '2027-01',
      desiredQuantity: 120,
    },
  ],

  permissions: {
    canManage: true,
    canCreateNegotiationRequest: false,
  },

  hasMyActiveNegotiationRequest: false,
} satisfies ProcurementRequestDetailData