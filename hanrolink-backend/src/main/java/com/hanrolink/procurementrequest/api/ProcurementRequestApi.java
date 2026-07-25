package com.hanrolink.procurementrequest.api;

import com.hanrolink.web.api.ApiPath;

public final class ProcurementRequestApi {

  public static final class V1 {

    public static final String BASE = ApiPath.API_V1 + "/procurement-requests";
    public static final String BY_ID = BASE + "/{procurementRequestId}";

    private V1() {}
  }

  private ProcurementRequestApi() {}
}
