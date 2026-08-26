package com.hanrolink.onboarding.request.component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OnboardingBusinessUserAccountRequest(
  @NotBlank
  @Size(max = 255)
  String lastName,

  @NotBlank
  @Size(max = 255)
  String firstName,

  @NotBlank
  @Pattern(
    regexp = "^[ァ-ヶー・]+$",
    message = "全角カタカナで入力してください"
  )
  @Size(max = 255)
  String lastNameKana,

  @NotBlank
  @Pattern(
    regexp = "^[ァ-ヶー・]+$",
    message = "全角カタカナで入力してください"
  )
  @Size(max = 255)
  String firstNameKana,

  @NotBlank
  @Size(max = 20)
  String phoneNumber
) {}
