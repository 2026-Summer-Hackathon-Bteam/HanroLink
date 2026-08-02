package com.hanrolink.onboarding.request;

import com.hanrolink.onboarding.request.component.OnboardingBusinessRequest;
import com.hanrolink.onboarding.request.component.OnboardingBusinessUserAccountRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record BuyerOnboardingRegisterRequest(
  @Valid
  @NotNull
  OnboardingBusinessRequest business,

  @Valid
  @NotNull
  OnboardingBusinessUserAccountRequest businessUserAccount
) {}
