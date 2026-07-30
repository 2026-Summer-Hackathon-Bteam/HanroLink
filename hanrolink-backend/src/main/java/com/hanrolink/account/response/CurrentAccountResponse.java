package com.hanrolink.account.response;

import com.hanrolink.account.enums.BusinessUserAccountRegistrationApiStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentAccountResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Role role,

  BusinessUserAccountRegistrationApiStatus businessUserAccountRegistrationStatus
) {

  public enum Role {
    ADMIN,
    SUPPLIER,
    BUYER
  }
}
