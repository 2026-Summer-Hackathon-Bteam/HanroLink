import type { components } from '../../shared/api/schema'
import { authenticatedApi } from '../../lib/api'

type OnboardingInitialData = components['schemas']['OnboardingGetResponse']

type OnboardingSubmission = components['schemas']['OnboardingCreateRequest']

type OnboardingSubmissionResult =
  components['schemas']['OnboardingCreateResponse']

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
  const { data, response } = await authenticatedApi.POST('/api/v1/onboarding', {
    body: request,
  })

  if (!response.ok || !data) {
    throw new Error(
      `事業者情報の登録に失敗しました。（ステータス：${response.status}）`,
    )
  }
  return data
}
