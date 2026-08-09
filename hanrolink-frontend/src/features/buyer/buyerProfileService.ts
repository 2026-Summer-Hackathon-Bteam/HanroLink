import { buyerProfileMock } from './buyerProfileMock'
import type { BuyerProfile } from './buyerProfileTypes'

export function getBuyerProfile(
  businessUserAccountId: string,
): Promise<BuyerProfile> {
  if (!businessUserAccountId) {
    return Promise.reject('バイヤーを特定できませんでした。')
  }
  return Promise.resolve(buyerProfileMock)
}
