import { productFormOptionsMock } from './productFormOptionMock'
import type {
  SupplierProductFormOptions,
  ProductImageUploadRequest,
  ProductImageUploadResponse,
} from './productFormTypes'
import { authenticatedApi } from '../../lib/api'

export function getProductFormOptions(): Promise<SupplierProductFormOptions> {
  return Promise.resolve(productFormOptionsMock)
}

export async function createProductImageUploadInformation({
  usage,
  fileSizeBytes,
}: ProductImageUploadRequest): Promise<ProductImageUploadResponse> {
  const { data, response } = await authenticatedApi.POST(
    '/api/v1/products/image-uploads',
    {
      body: {
        usage,
        fileSizeBytes,
      },
    },
  )

  if (!response.ok || !data) {
    throw new Error('画像アップロードURLの取得に失敗しました。')
  }

  return data
}

export async function uploadProductImage(
  uploadUrl: string,
  imageFile: Blob,
): Promise<void> {
  const response = await fetch(uploadUrl, {
    method: 'PUT',
    headers: {
      'Content-Type': 'image/webp',
    },
    body: imageFile,
  })

  if (!response.ok) {
    throw new Error('画像のアップロードに失敗しました。')
  }
}
