import type { components } from '../../shared/api/schema'
import { authenticatedApi } from '../../lib/api'
import { getApiErrorMessage } from '../../shared/api/apiError'

type OnboardingInitialData = components['schemas']['OnboardingGetResponse']

type OnboardingSubmission = components['schemas']['OnboardingCreateRequest']

type OnboardingSubmissionResult =
  components['schemas']['OnboardingCreateResponse']

const onboardingFieldLabels: Record<string, string> = {
  'business.role': '事業者区分',
  'business.name': '事業者名',
  'business.nameKana': '事業者名カナ',
  'business.websiteUrl': 'ホームページ',
  'business.addressPostalCode': '郵便番号',
  'business.addressPrefecture': '都道府県',
  'business.addressMunicipalityStreet': '市区町村・番地',
  'business.addressBuilding': '建物名',
  'business.phoneNumber': '事業者電話番号',
  'businessUserAccount.lastName': '担当者の姓',
  'businessUserAccount.firstName': '担当者の名',
  'businessUserAccount.lastNameKana': '担当者の姓カナ',
  'businessUserAccount.firstNameKana': '担当者の名カナ',
  'businessUserAccount.phoneNumber': '担当者電話番号',
}

export async function getOnboardingInitialData(): Promise<OnboardingInitialData> {
  const { data, response } = await authenticatedApi.GET('/api/v1/onboarding')

  if (!response.ok || !data) {
    throw new Error(
      `オンボーディング情報の取得に失敗しました（ステータス:${response.status}）`,
    )
  }
  return data
}

export async function submitOnboarding(
  request: OnboardingSubmission,
): Promise<OnboardingSubmissionResult> {
  const { data, error, response } = await authenticatedApi.POST(
    '/api/v1/onboarding',
    {
      body: request,
    },
  )

  if (!response.ok || !data) {
    throw new Error(
      getApiErrorMessage(
        error,
        `事業者情報の登録に失敗しました。（ステータス：${response.status}）`,
        onboardingFieldLabels,
      ),
    )
  }
  return data
}
