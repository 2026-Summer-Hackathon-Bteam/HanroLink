import type { BuyerMyPageData } from './buyerMyPageTypes'

export const buyerMyPageMock = {
  business: {
    businessName: 'サンプル百貨店株式会社',
  },

  receivedNegotiations: [
    {
      id: 1,
      procurementRequest: {
        id: 101,
        title: '国産いちごを使用した加工食品を募集',
      },
      product: {
        id: 201,
        name: '濃厚いちごジャム',
        businessName: 'サンプル食品株式会社',
      },
      expiresAt: '2026-08-31T23:59:59+09:00',
    },
    {
      id: 2,
      procurementRequest: {
        id: 102,
        title: '常温保存できる果汁飲料を募集',
      },
      product: {
        id: 202,
        name: '青森県産りんごジュース',
        businessName: 'テスト飲料株式会社',
      },
      expiresAt: '2026-09-15T23:59:59+09:00',
    },
  ],

  sentNegotiations: [
    {
      id: 301,
      product: {
        id: 201,
        name: '濃厚いちごジャム',
      },
      expiresAt: '2026-08-31T23:59:59+09:00',
    },
    {
      id: 302,
      product: {
        id: 203,
        name: '国産みかんゼリー',
      },
      expiresAt: '2026-09-10T23:59:59+09:00',
    },
  ],

  procurementRequests: [
    {
      id: "101",
      title: '国産いちごを使用した加工食品を募集',
      updatedAt: '2026-08-04T10:30:00+09:00',
    },
    {
      id: "102",
      title: '常温保存できる果汁飲料を募集',
      updatedAt: '2026-08-02T14:00:00+09:00',
    },
    {
      id: "103",
      title: '秋冬向けの焼き菓子を募集',
      updatedAt: '2026-07-28T09:15:00+09:00',
    },
  ],

  chats: [
    {
      id: '11111111-1111-1111-1111-111111111111',
      name: '濃厚いちごジャム_サンプル食品株式会社',
      lastActivityAt: '2026-08-04T15:30:00+09:00',
    },
    {
      id: '22222222-2222-2222-2222-222222222222',
      name: '常温保存できる果汁飲料を募集_テスト飲料株式会社',
      lastActivityAt: '2026-08-03T11:20:00+09:00',
    },
  ],
} satisfies BuyerMyPageData