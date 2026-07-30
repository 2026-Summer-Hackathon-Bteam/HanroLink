package com.hanrolink.onboarding.api;

import com.hanrolink.web.api.ApiPath;

public final class OnboardingApi {

  public static final class V1 {

    public static final String BASE = ApiPath.API_V1 + "/onboarding";
    public static final String SUPPLIER = BASE + "/supplier";
    public static final String BUYER = BASE + "/buyer";

    private V1() {}
  }

  private OnboardingApi() {}
}
