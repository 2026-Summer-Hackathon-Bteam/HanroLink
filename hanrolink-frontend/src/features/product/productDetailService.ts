import type { ProductDetail } from './productDetailTypes'
import { productDetailMock } from './productDetailMock'

export function getProductDetailData(
  productId: number,
): Promise<ProductDetail> {
  if (productId !== 1) return Promise.reject('データがありません。')
  return Promise.resolve(productDetailMock)
}

export function deleteProduct(productId: string): Promise<void> {
  if (productId !== productDetailMock.id) {
    return Promise.reject(new Error('対象の商品が見つかりません。'))
  }

  return Promise.resolve()
}

export function updateProductVisibility(
  productId: string,
  hidden: boolean,
): Promise<void> {
  if (productId !== productDetailMock.id) {
    return Promise.reject(new Error('対象の商品が見つかりません。'))
  }

  // Mockではデータ自体を書き換えないため、現時点では値を使用しない
  void hidden

  return Promise.resolve()
}
