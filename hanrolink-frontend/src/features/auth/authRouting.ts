export type CurrentAccount =
  | {
      role: 'ADMIN'
      businessUserAccountRegistrationStatus: null
    }
  | {
      role: null
      businessUserAccountRegistrationStatus: 'NOT_SUBMITTED'
    }
  | {
      role: 'SUPPLIER' | 'BUYER'
      businessUserAccountRegistrationStatus: 'PENDING' | 'APPROVED'
    }

const myPagePathByRole = {
  SUPPLIER: '/mypage/supplier',
  BUYER: '/mypage/buyer',
} as const

export function getPathAfterLogin(account: CurrentAccount): string | undefined {
  const { role, businessUserAccountRegistrationStatus: status } = account

  if (role === 'ADMIN') {
    return '/mypage/admin'
  }

  if (role === null && status === 'NOT_SUBMITTED') {
    return '/onboarding/business'
  }

  if (
    (status === 'PENDING' || status === 'APPROVED') &&
    (role === 'SUPPLIER' || role === 'BUYER')
  ) {
    return myPagePathByRole[role]
  }

  return undefined
}
