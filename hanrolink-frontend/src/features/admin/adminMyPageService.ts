import type { PendingBusinessRegistration } from './adminMyPageTypes'
import { adminMyPageMock } from './adminMyPageMock'

export function getPendingBusinessRegistrations(): Promise<
  PendingBusinessRegistration[]
> {
  return Promise.resolve(adminMyPageMock)
}
