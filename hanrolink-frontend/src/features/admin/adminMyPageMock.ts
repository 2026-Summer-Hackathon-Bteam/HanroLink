import type { PendingBusinessRegistration } from './adminMyPageTypes'

export const adminMyPageMock = [
  {
    businessUserAccountId: '11111111-1111-1111-1111-111111111111',
    businessName: '津軽りんごファーム株式会社',
    createdAt: '2026-08-05T09:15:00+09:00',
  },
  {
    businessUserAccountId: '22222222-2222-2222-2222-222222222222',
    businessName: '青森フーズ株式会社',
    createdAt: '2026-08-04T14:30:00+09:00',
  },
  {
    businessUserAccountId: '33333333-3333-3333-3333-333333333333',
    businessName: '北日本食品販売株式会社',
    createdAt: '2026-08-03T11:20:00+09:00',
  },
  {
    businessUserAccountId: '44444444-4444-4444-4444-444444444444',
    businessName: 'みちのく農産合同会社',
    createdAt: '2026-08-02T16:45:00+09:00',
  },
  {
    businessUserAccountId: '55555555-5555-5555-5555-555555555555',
    businessName: 'サンプル加工食品株式会社',
    createdAt: '2026-08-01T10:00:00+09:00',
  },
] satisfies PendingBusinessRegistration[]

