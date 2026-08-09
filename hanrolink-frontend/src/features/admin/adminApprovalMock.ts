import type { AdminBusinessApprovalDetail } from './adminApprovalTypes'

export const adminApprovalMock = {
  businessUserAccount: {
    id: '11111111-1111-1111-1111-111111111111',
    role: 'SUPPLIER',
    reviewStatus: 'PENDING',
    lastName: '津軽',
    firstName: '太郎',
    lastNameKana: 'ツガル',
    firstNameKana: 'タロウ',
    phoneNumber: '09012345678',
    email: 'tsugaru@example.com',
    createdAt: '2026-08-05T09:15:00+09:00',
  },

  business: {
    name: '津軽りんごファーム株式会社',
    nameKana: 'ツガルリンゴファームカブシキガイシャ',
    websiteUrl: 'https://example.com',
    addressPostalCode: '0368084',
    addressPrefecture: '青森県',
    addressMunicipalityStreet: '弘前市高田1丁目2-3',
    addressBuilding: 'サンプル農業センター2階',
    phoneNumber: '0172123456',
  },
} satisfies AdminBusinessApprovalDetail