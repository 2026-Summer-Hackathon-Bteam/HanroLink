package com.hanrolink.account.response;

import com.hanrolink.account.enums.BusinessUserAccountRegistrationApiStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentAccountResponse(
  @Schema(
    requiredMode = Schema.RequiredMode.REQUIRED,
    nullable = true
  )
  CurrentAccountRole role,

  
  @Schema(
    requiredMode = Schema.RequiredMode.REQUIRED,
    nullable = true
  )
  BusinessUserAccountRegistrationApiStatus businessUserAccountRegistrationStatus
) {

  public enum CurrentAccountRole {
    ADMIN,
    SUPPLIER,
    BUYER
  }
}
