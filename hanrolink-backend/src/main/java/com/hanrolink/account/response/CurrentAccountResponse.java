package com.hanrolink.account.response;

import com.hanrolink.account.enums.BusinessUserAccountRegistrationApiStatus;

public record CurrentAccountResponse(
  Role role,

  BusinessUserAccountRegistrationApiStatus businessUserAccountRegistrationStatus
) {

  public enum Role {
    ADMIN,
    SUPPLIER,
    BUYER
  }
}
