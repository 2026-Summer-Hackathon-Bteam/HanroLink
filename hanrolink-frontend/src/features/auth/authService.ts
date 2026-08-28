import {
  confirmSignUp,
  signIn,
  signUp,
  fetchAuthSession,
  signOut,
  confirmSignIn,
  type ConfirmSignUpOutput,
  type SignInOutput,
  type SignUpOutput,
  type ConfirmSignInOutput,
} from 'aws-amplify/auth'

type SignUpUserInput = {
  email: string
  password: string
}

type ConfirmSignUpUserInput = {
  email: string
  confirmationCode: string
}

type SignInUserInput = {
  email: string
  password: string
}

type ConfirmInitialPasswordInput = {
  newPassword: string
}

export function signUpUser({
  email,
  password,
}: SignUpUserInput): Promise<SignUpOutput> {
  return signUp({
    username: email.trim(),
    password,
    options: {
      userAttributes: {
        email: email.trim(),
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

export async function getAccessToken(): Promise<string> {
  const session = await fetchAuthSession()
  const accessToken = session.tokens?.accessToken

  if (!accessToken) {
    throw new Error('アクセストークンを取得できませんでした。')
  }

  return accessToken.toString()
}

export function signOutUser(): Promise<void> {
  return signOut()
}

export function confirmInitialPassword({
  newPassword,
}: ConfirmInitialPasswordInput): Promise<ConfirmSignInOutput> {
  return confirmSignIn({
    challengeResponse: newPassword,
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
    case 'TooManyFailedAttemptsException':
    case 'TooManyRequestsException':
      return '試行回数または送信回数が上限に達しました。時間をおいて再度お試しください。'
    case 'UserNotFoundException':
    case 'NotAuthorizedException':
      return '入力された内容に誤りがあります。内容を確認して再度お試しください。'
    case 'UserNotConfirmedException':
      return 'メールアドレスの確認が完了していません。'
    case 'PasswordResetRequiredException':
      return 'パスワードの再設定が必要です。'
    case 'NetworkError':
      return '通信に失敗しました。通信環境を確認してください。'
    case 'SignInException':
      return 'ログイン処理を確認できません。ログイン画面からやり直してください。'
    default:
      return '認証処理に失敗しました。時間をおいて再度お試しください。'
  }
}
