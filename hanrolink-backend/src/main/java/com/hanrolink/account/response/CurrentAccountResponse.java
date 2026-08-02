package com.hanrolink.account.response;

import com.hanrolink.account.enums.BusinessUserAccountRegistrationApiStatus;

public record CurrentAccountResponse(
  CurrentAccountRole role,

  BusinessUserAccountRegistrationApiStatus businessUserAccountRegistrationStatus
) {

  public enum CurrentAccountRole {
    ADMIN,
    SUPPLIER,
    BUYER
  }
}
