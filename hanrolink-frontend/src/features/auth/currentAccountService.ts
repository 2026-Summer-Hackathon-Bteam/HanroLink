import type { components } from '../../shared/api/schema'
import { authenticatedApi } from '../../lib/api'
import type { CurrentAccount } from './authRouting'

type CurrentAccountResponse =
  components['schemas']['CurrentAccountGetResponse']

function parseCurrentAccount(
  response: CurrentAccountResponse,
): CurrentAccount {
  const {
    role,
    businessUserAccountRegistrationStatus: status,
  } = response

  if (role === 'ADMIN' && status === null) {
    return {
      role,
      businessUserAccountRegistrationStatus: status,
    }
  }

  if (role === null && status === 'NOT_SUBMITTED') {
    return {
      role,
      businessUserAccountRegistrationStatus: status,
    }
  }

  if (
    (role === 'SUPPLIER' || role === 'BUYER') &&
    (status === 'PENDING' || status === 'APPROVED')
  ) {
    return {
      role,
      businessUserAccountRegistrationStatus: status,
    }
  }

  throw new Error('自己情報のレスポンスが想定した形式ではありません。')
}

export async function getCurrentAccount(): Promise<CurrentAccount> {
  const { data, response } = await authenticatedApi.GET('/api/v1/me')

  if (!response.ok || !data) {
    throw new Error(
      `自己情報の取得に失敗しました。（ステータス: ${response.status}）`,
    )
  }

  return parseCurrentAccount(data)
}