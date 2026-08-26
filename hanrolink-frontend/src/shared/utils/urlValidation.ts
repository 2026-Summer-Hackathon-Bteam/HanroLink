export function getSafeExternalUrl(
  value: string | null | undefined,
): string | null {
  if (!value) return null

  try {
    const url = new URL(value)

    if (url.protocol !== 'http:' && url.protocol !== 'https:') {
      return null
    }

    if (url.username || url.password) {
      return null
    }

    return url.toString()
  } catch {
    return null
  }
}

const cloudFrontDomainName =
  import.meta.env.VITE_CLOUDFRONT_DOMAIN_NAME?.trim().toLowerCase()

export function getSafeChatFileUrl(
  value: string | null | undefined,
): string | null {
  if (!value || !cloudFrontDomainName) return null

  try {
    const url = new URL(value)

    if (
      url.protocol !== 'https:' ||
      url.hostname !== cloudFrontDomainName ||
      url.port ||
      url.username ||
      url.password
    ) {
      return null
    }

    return url.toString()
  } catch {
    return null
  }
}
