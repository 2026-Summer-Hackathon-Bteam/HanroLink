package com.hanrolink.account.api;

import com.hanrolink.web.api.ApiPath;

public final class AccountApi {

  public static final class V1 {

    public static final String ME = ApiPath.API_V1 + "/me";
    public static final String BUYER = ApiPath.API_V1 + "/buyer/{businessUserAccountId}";

    private V1() {}
  }

  private AccountApi() {}
}
