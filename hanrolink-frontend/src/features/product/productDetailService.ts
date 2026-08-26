import type { ProductDetail } from './productDetailTypes'
import { authenticatedApi } from '../../lib/api'

export async function getProductDetailData(
  productId: string,
): Promise<ProductDetail> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/products/{productId}',
    {
      params: {
        path: {
          productId,
        },
      },
    },
  )

  if (!response.ok || !data) {
    throw new Error('商品詳細情報の取得に失敗しました。')
  }

  return data
}

export async function deleteProduct(productId: string): Promise<void> {
  const { response } = await authenticatedApi.DELETE(
    '/api/v1/products/{productId}',
    {
      params: {
        path: {
          productId,
        },
      },
    },
  )

  if (!response.ok || response.status !== 204) {
    throw new Error('商品情報の削除に失敗しました。')
  }
}

export async function updateProductVisibility(
  productId: string,
  hidden: boolean,
): Promise<void> {
  const { response } = await authenticatedApi.PATCH(
    '/api/v1/products/{productId}/visibility',
    {
      params: {
        path: {
          productId,
        },
      },
      body: {
        hidden,
      },
    },
  )

  if (!response.ok || response.status !== 204) {
    throw new Error('商品の非表示設定の切り替えに失敗しました。')
  }
}
