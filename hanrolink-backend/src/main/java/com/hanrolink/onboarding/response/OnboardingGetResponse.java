package com.hanrolink.onboarding.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record OnboardingGetResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String email
) {}
