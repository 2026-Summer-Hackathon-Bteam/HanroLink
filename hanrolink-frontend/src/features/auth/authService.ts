import {
  confirmSignUp,
  signIn,
  signUp,
  type ConfirmSignUpOutput,
  type SignInOutput,
  type SignUpOutput,
} from 'aws-amplify/auth'

export type Role = 'supplier' | 'buyer'

type SignUpUserInput = {
  email: string
  password: string
  role: Role
}

type ConfirmSignUpUserInput = {
  email: string
  confirmationCode: string
}

type SignInUserInput = {
  email: string
  password: string
}

export function signUpUser({
  email,
  password,
  role,
}: SignUpUserInput): Promise<SignUpOutput> {
  return signUp({
    username: email.trim(),
    password,
    options: {
      userAttributes: {
        email: email.trim(),
        'custom:role': role,
      },
    },
  })
}

export function confirmSignUpUser({
  email,
  confirmationCode,
}: ConfirmSignUpUserInput): Promise<ConfirmSignUpOutput> {
  return confirmSignUp({
    username: email.trim(),
    confirmationCode: confirmationCode.trim(),
  })
}

export function signInUser({
  email,
  password,
}: SignInUserInput): Promise<SignInOutput> {
  return signIn({
    username: email.trim(),
    password,
  })
}

export function getAuthErrorMessage(error: unknown): string {
  const errorName =
    typeof error === 'object' &&
    error !== null &&
    'name' in error &&
    typeof error.name === 'string'
      ? error.name
      : ''

  switch (errorName) {
    case 'UsernameExistsException':
      return 'このメールアドレスは既に登録されています。'
    case 'InvalidPasswordException':
      return 'パスワードがCognitoの設定条件を満たしていません。'
    case 'CodeMismatchException':
      return '確認コードが正しくありません。'
    case 'ExpiredCodeException':
      return '確認コードの有効期限が切れています。'
    case 'LimitExceededException':
    case 'TooManyRequestsException':
      return '操作回数が上限に達しました。時間をおいて再度お試しください。'
    case 'UserNotFoundException':
    case 'NotAuthorizedException':
      return 'メールアドレスまたはパスワードが正しくありません。'
    case 'UserNotConfirmedException':
      return 'メールアドレスの確認が完了していません。'
    case 'PasswordResetRequiredException':
      return 'パスワードの再設定が必要です。'
    case 'NetworkError':
      return '通信に失敗しました。通信環境を確認してください。'
    default:
      return '認証処理に失敗しました。時間をおいて再度お試しください。'
  }
}
