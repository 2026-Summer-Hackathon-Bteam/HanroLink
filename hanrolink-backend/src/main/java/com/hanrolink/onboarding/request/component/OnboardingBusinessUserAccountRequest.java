package com.hanrolink.onboarding.request.component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OnboardingBusinessUserAccountRequest(
  @NotBlank
  @Size(max = 255)
  String lastName,

  @NotBlank
  @Size(max = 255)
  String firstName,

  @NotBlank
  @Size(max = 255)
  String lastNameKana,

  @NotBlank
  @Size(max = 255)
  String firstNameKana,

  @NotBlank
  @Size(max = 20)
  String phoneNumber
  ) {}
