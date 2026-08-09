import type { BuyerProfile } from './buyerProfileTypes'

export const buyerProfileMock = {
  businessName: 'サンプル百貨店株式会社',
  businessAddressPrefecture: '東京都',
  businessAddressMunicipalityStreet: '新宿区西新宿1丁目2番3号',
  businessAddressBuilding: 'サンプル新宿ビル10階',
  websiteUrl: 'https://example.com',
} satisfies BuyerProfile
