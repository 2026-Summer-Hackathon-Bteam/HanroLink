package com.hanrolink.onboarding.response;

import java.util.Objects;

import com.hanrolink.business.enums.BusinessRegistrationApiStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record OnboardingCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessRegistrationApiStatus businessUserAccountRegistrationStatus
) {
  public OnboardingCreateResponse {
    Objects.requireNonNull(
      businessUserAccountRegistrationStatus,
      "OnboardingCreateResponse.businessUserAccountRegistrationStatus must not be null"
    );
  }
}
