// ここでAPI通信の設定を1か所にまとめる
import createClient from 'openapi-fetch'
import type { paths } from '../shared/api/schema'
import { getAccessToken } from '../features/auth/authService'

const createApiClient = () =>
  createClient<paths>({
    baseUrl: import.meta.env.VITE_API_URL,
  })

// ログイン前のAPIで使用
export const api = createApiClient()

// ログイン後のAPIで使用
export const authenticatedApi = createApiClient()

authenticatedApi.use({
  async onRequest({ request }) {
    const accessToken = await getAccessToken()

    request.headers.set('Authorization', `Bearer ${accessToken}`)

    return request
  },
})
