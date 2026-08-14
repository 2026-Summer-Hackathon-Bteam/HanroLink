package com.hanrolink.account.response;

import com.hanrolink.business.enums.BusinessRegistrationApiStatus;
import com.hanrolink.security.authorization.enums.ApplicationRole;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentAccountGetResponse(
  @Schema(
    requiredMode = Schema.RequiredMode.REQUIRED,
    nullable = true
  )
  ApplicationRole role,

  
  @Schema(
    requiredMode = Schema.RequiredMode.REQUIRED,
    nullable = true
  )
  BusinessRegistrationApiStatus businessUserAccountRegistrationStatus
) {}
