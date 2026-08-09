import { productFormOptionsMock } from './productFormOptionMock'
import type { SupplierProductFormOptions } from './productFormTypes'

export function getProductFormOptions(): Promise<SupplierProductFormOptions> {
  return Promise.resolve(productFormOptionsMock)
}
