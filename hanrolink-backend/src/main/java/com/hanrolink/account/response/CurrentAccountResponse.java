package com.hanrolink.account.response;

import com.hanrolink.account.enums.AccountRole;
import com.hanrolink.account.enums.BusinessUserAccountRegistrationApiStatus;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentAccountResponse(
  @Schema(
    requiredMode = Schema.RequiredMode.REQUIRED,
    nullable = true
  )
  AccountRole role,

  
  @Schema(
    requiredMode = Schema.RequiredMode.REQUIRED,
    nullable = true
  )
  BusinessUserAccountRegistrationApiStatus businessUserAccountRegistrationStatus
) {}
