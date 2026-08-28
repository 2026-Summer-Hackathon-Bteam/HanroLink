package com.hanrolink.onboarding.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record OnboardingGetResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String email
) {
  public OnboardingGetResponse {
    Objects.requireNonNull(
      email,
      "OnboardingGetResponse.email must not be null"
    );
  }
}
