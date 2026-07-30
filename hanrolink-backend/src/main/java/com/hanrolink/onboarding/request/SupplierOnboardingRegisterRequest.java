package com.hanrolink.onboarding.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SupplierOnboardingRegisterRequest(
  @Valid
  @NotNull
  Business business,

  @Valid
  @NotNull
  BusinessUserAccount businessUserAccount
) {

  public record Business(
    @NotBlank
    @Size(max = 255)
    String name,

    @NotBlank
    @Size(max = 255)
    String nameKana,

    @URL
    @Size(max = 255)
    String websiteUrl,

    @NotBlank
    @Pattern(
      regexp = "[0-9]{7}",
      message = "ハイフンなしの半角数字7桁で入力してください"
    )
    String addressPostalCode,

    @NotBlank
    @Size(max = 50)
    String addressPrefecture,

    @NotBlank
    @Size(max = 255)
    String addressMunicipalityStreet,

    @Size(max = 255)
    String addressBuilding,

    @NotBlank
    @Size(max = 20)
    String phoneNumber
  ) {}

  public record BusinessUserAccount(
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
}