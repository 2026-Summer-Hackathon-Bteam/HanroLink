package com.hanrolink.infrastructure.cloudfront;

import java.time.Duration;

public record CloudFrontSignedCookieValues(
  String policyHeaderValue,
  String signatureHeaderValue,
  String keyPairIdHeaderValue,
  String cookiePath,
  Duration validDuration
) {}
