import { productFormOptionsMock } from './productFormOptionMock'
import type {
  SupplierProductFormOptions,
  ProductImageUploadRequest,
  ProductImageUploadResponse,
  SupplierProductCreateRequest,
  SupplierProductCreateResponse,
  ProductImageUsage,
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

export async function createProduct(
  request: SupplierProductCreateRequest,
): Promise<SupplierProductCreateResponse> {
  const { data, response } = await authenticatedApi.POST('/api/v1/products', {
    body: request,
  })

  if (!response.ok || !data) {
    throw new Error('商品の登録に失敗しました。')
  }

  return data
}

export async function uploadPreparedProductImage(
  imageBlob: Blob,
  usage: ProductImageUsage,
): Promise<string> {
  if (imageBlob.type !== 'image/webp') {
    throw new Error('WebP形式への変換に失敗しました。')
  }
  // アップロードURLとIDの取得
  const { uploadUrl, pendingFileUploadId } =
    await createProductImageUploadInformation({
      usage,
      fileSizeBytes: imageBlob.size,
    })
  // S3への画像アップロード
  await uploadProductImage(uploadUrl, imageBlob)

  return pendingFileUploadId
}
