package com.hanrolink.businessapproval.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record BusinessApprovalAccountResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String lastName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String firstName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String lastNameKana,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String firstNameKana,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String phoneNumber,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String email
) {
  public BusinessApprovalAccountResponse {
    Objects.requireNonNull(
      lastName,
      "BusinessApprovalAccountResponse.lastName must not be null"
    );

    Objects.requireNonNull(
      firstName,
      "BusinessApprovalAccountResponse.firstName must not be null"
    );

    Objects.requireNonNull(
      lastNameKana,
      "BusinessApprovalAccountResponse.lastNameKana must not be null"
    );

    Objects.requireNonNull(
      firstNameKana,
      "BusinessApprovalAccountResponse.firstNameKana must not be null"
    );

    Objects.requireNonNull(
      phoneNumber,
      "BusinessApprovalAccountResponse.phoneNumber must not be null"
    );

    Objects.requireNonNull(
      email,
      "BusinessApprovalAccountResponse.email must not be null"
    );
  }
}
