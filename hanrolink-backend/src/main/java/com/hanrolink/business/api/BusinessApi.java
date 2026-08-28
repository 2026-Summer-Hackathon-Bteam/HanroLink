package com.hanrolink.business.api;

import com.hanrolink.web.api.ApiPath;

public final class BusinessApi {

  public static final class V1 {

    public static final String BUYER = ApiPath.API_V1 + "/buyer/{businessId}";
    public static final String MINE = ApiPath.API_V1 + "/me/business";

    private V1() {}
  }

  private BusinessApi() {}
}
