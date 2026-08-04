import mainVisual from '../../assets/mainvisual.png'
import type { SupplierMyPageData } from './supplierMyPageTypes'

export const supplierMyPageMock = {
  business: {
    businessName: 'サンプル食品株式会社',
  },
  
  receivedNegotiations: [
    {
      productNegotiationRequestId: 1,
      product: {
        id: 101,
        name: 'いちごジャム',
      },
      buyer: {
        accountId: '11111111-1111-1111-1111-111111111111',
        businessName: 'サンプル商事',
      },
      expiresAt: '2026-08-31T23:59:59+09:00',
    },
    {
      productNegotiationRequestId: 2,
      product: {
        id: 102,
        name: 'りんごジュース',
      },
      buyer: {
        accountId: '22222222-2222-2222-2222-222222222222',
        businessName: 'テスト食品株式会社',
      },
      expiresAt: '2026-09-15T23:59:59+09:00',
    },
  ],

  sentNegotiations: [
    {
      procurementNegotiationRequestId: 201,
      procurementRequest: {
        id: 301,
        title: '国産果物を使用した加工食品を募集',
      },
      product: {
        id: 101,
        name: 'いちごジャム',
      },
      expiresAt: '2026-08-31T23:59:59+09:00',
    },
  ],

  products: [
    {
      id: 101,
      name: 'いちごジャム',
      mainImageUrl: mainVisual,
      hidden: false,
      updatedAt: '2026-08-01T10:30:00+09:00',
    },
    {
      id: 102,
      name: 'りんごジュース',
      mainImageUrl: mainVisual,
      hidden: true,
      updatedAt: '2026-07-28T15:00:00+09:00',
    },
  ],

  chats: [
    {
      id: '33333333-3333-3333-3333-333333333333',
      name: 'いちごジャム_サンプル商事',
      lastActivityAt: '2026-08-03T12:00:00+09:00',
    },
    {
      id: '44444444-4444-4444-4444-444444444444',
      name: '国産果物を使用した加工食品を募集_テスト食品株式会社',
      lastActivityAt: '2026-08-02T18:30:00+09:00',
    },
  ],
} satisfies SupplierMyPageData
