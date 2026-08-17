import type { GuestData } from './guestTypes'
import { guestDataMock } from './guestMock'

export function getGuestData(): Promise<GuestData> {
  return Promise.resolve(guestDataMock)
}
