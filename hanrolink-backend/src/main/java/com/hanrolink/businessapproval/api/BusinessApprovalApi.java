package com.hanrolink.businessapproval.api;

import com.hanrolink.web.api.ApiPath;

public final class BusinessApprovalApi {
  public static final class V1 {

    private static final String BASE = ApiPath.API_V1 + "/admin/business-registrations";
    public static final String PENDING = BASE + "/pending";
    public static final String BY_ID = BASE + "/{businessUserAccountId}";
    public static final String APPROVE = BY_ID + "/approve";

    private V1() {}
  }

  private BusinessApprovalApi() {}  
}
