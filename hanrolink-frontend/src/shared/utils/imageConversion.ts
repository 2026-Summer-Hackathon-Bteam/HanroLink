const MAX_FILE_SIZE_BYTES = 300 * 1024
const MAX_PIXEL_COUNT = 4_000_000
const RESIZE_RATIO = 0.85
const MAX_RESIZE_ATTEMPTS = 8
const WEBP_QUALITIES = [0.85, 0.7, 0.55, 0.4]

const supportedImageTypes = new Set([
  'image/jpeg',
  'image/png',
  'image/webp',
  'image/heic',
  'image/heif',
])

function canvasToWebp(
  canvas: HTMLCanvasElement,
  quality: number,
): Promise<Blob> {
  return new Promise((resolve, reject) => {
    canvas.toBlob(
      (blob) => {
        if (!blob) {
          reject(
            new Error(
              'この端末では写真をアップロード用に変換できませんでした。OSまたはブラウザを更新して、もう一度お試しください。（WebP変換処理に失敗）',
            ),
          )
          return
        }

        if (blob.type !== 'image/webp') {
          reject(
            new Error(
              `この端末では写真をアップロード用に変換できませんでした。OSまたはブラウザを更新して、もう一度お試しください。（WebP変換処理に失敗／出力形式：${blob.type || '不明'}）`,
            ),
          )
          return
        }

        resolve(blob)
      },
      'image/webp',
      quality,
    )
  })
}

export async function convertImageToWebp(file: File): Promise<Blob> {
  if (!supportedImageTypes.has(file.type)) {
    throw new Error(
      `この画像は使用できません。別の写真を選択してください。（対応形式：JPEG、PNG、WebP、HEIC、HEIF／選択した画像の形式：${file.type || '不明'}）`,
    )
  }

  if (file.size === 0) {
    throw new Error('空の画像ファイルは使用できません。')
  }

  let imageBitmap: ImageBitmap

  try {
    imageBitmap = await createImageBitmap(file)
  } catch {
    throw new Error(
      `この写真を読み込めませんでした。別の写真を選ぶか、OSまたはブラウザを更新してからもう一度お試しください。（画像読み込み処理に失敗／画像形式：${file.type || '不明'}）`,
    )
  }

  try {
    const initialScale = Math.min(
      1,
      Math.sqrt(MAX_PIXEL_COUNT / (imageBitmap.width * imageBitmap.height)),
    )

    let width = Math.max(1, Math.floor(imageBitmap.width * initialScale))
    let height = Math.max(1, Math.floor(imageBitmap.height * initialScale))

    const canvas = document.createElement('canvas')
    const context = canvas.getContext('2d')

    if (!context) {
      throw new Error('画像を変換する準備に失敗しました。')
    }

    for (
      let resizeAttempt = 0;
      resizeAttempt < MAX_RESIZE_ATTEMPTS;
      resizeAttempt += 1
    ) {
      canvas.width = width
      canvas.height = height

      context.clearRect(0, 0, width, height)
      context.drawImage(imageBitmap, 0, 0, width, height)

      for (const quality of WEBP_QUALITIES) {
        const blob = await canvasToWebp(canvas, quality)

        if (blob.size <= MAX_FILE_SIZE_BYTES) {
          return blob
        }
      }

      width = Math.max(1, Math.floor(width * RESIZE_RATIO))
      height = Math.max(1, Math.floor(height * RESIZE_RATIO))
    }

    throw new Error(
      '写真の容量を小さくできませんでした。別の写真を選択してください。（WebP変換後の上限：300KB）',
    )
  } finally {
    imageBitmap.close()
  }
}
