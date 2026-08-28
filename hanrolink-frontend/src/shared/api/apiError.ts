type ApiProblemDetail = {
  detail?: unknown
  errors?: unknown
}

const isRecord = (value: unknown): value is Record<string, unknown> =>
  typeof value === 'object' && value !== null

export const getApiErrorMessage = (
  error: unknown,
  fallbackMessage: string,
  fieldLabels: Record<string, string> = {},
): string => {
  if (!isRecord(error)) return fallbackMessage

  const problem = error as ApiProblemDetail

  if (isRecord(problem.errors)) {
    const validationMessages = Object.entries(problem.errors).flatMap(
      ([field, messages]) => {
        if (!Array.isArray(messages)) return []

        const label = fieldLabels[field] ?? field

        return messages
          .filter((message): message is string => typeof message === 'string')
          .map((message) => `${label}：${message}`)
      },
    )

    if (validationMessages.length > 0) {
      return [...new Set(validationMessages)].join('\n')
    }
  }

  return typeof problem.detail === 'string' && problem.detail
    ? problem.detail
    : fallbackMessage
}