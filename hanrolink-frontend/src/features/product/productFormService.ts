import type {
  SupplierProductFormOptions,
  ProductImageUploadRequest,
  ProductImageUploadResponse,
  SupplierProductCreateRequest,
  SupplierProductCreateResponse,
  ProductImageUsage,
  SupplierProductUpdateRequest,
} from './productFormTypes'
import { authenticatedApi } from '../../lib/api'
import { getApiErrorMessage } from '../../shared/api/apiError'
import { productFieldLabels } from './productApiError'

export async function getProductFormOptions(): Promise<SupplierProductFormOptions> {
  const { data, response } = await authenticatedApi.GET(
    '/api/v1/products/form-options',
  )

  if (!response.ok || !data) {
    throw new Error(
      'ストーリーのテンプレートおよびフォーム選択肢の取得に失敗しました。',
    )
  }

  return data
}

export async function createProductImageUploadInformation({
  usage,
  fileSizeBytes,
}: ProductImageUploadRequest): Promise<ProductImageUploadResponse> {
  const { data, error, response } = await authenticatedApi.POST(
    '/api/v1/products/image-uploads',
    {
      body: {
        usage,
        fileSizeBytes,
      },
    },
  )

  if (!response.ok || !data) {
    throw new Error(
      getApiErrorMessage(
        error,
        '画像アップロードURLの取得に失敗しました。',
        productFieldLabels,
      ),
    )
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
  const { data, error, response } = await authenticatedApi.POST(
    '/api/v1/products',
    {
      body: request,
    },
  )

  if (!response.ok || !data) {
    throw new Error(
      getApiErrorMessage(
        error,
        '商品の登録に失敗しました。',
        productFieldLabels,
      ),
    )
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

export async function updateProduct(
  productId: string,
  request: SupplierProductUpdateRequest,
): Promise<void> {
  const { error, response } = await authenticatedApi.PUT(
    '/api/v1/products/{productId}',
    {
      params: {
        path: {
          productId,
        },
      },
      body: request,
    },
  )

  if (!response.ok || response.status !== 204) {
    throw new Error(
      getApiErrorMessage(
        error,
        '商品の更新に失敗しました。',
        productFieldLabels,
      ),
    )
  }
}
