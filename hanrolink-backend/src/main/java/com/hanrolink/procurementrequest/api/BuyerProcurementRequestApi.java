package com.hanrolink.procurementrequest.api;

import com.hanrolink.web.api.ApiPath;

public final class BuyerProcurementRequestApi {

  public static final class V1 {

    public static final String BASE = ApiPath.API_V1 + "/buyer/procurement-requests";
    public static final String BY_ID = BASE + "/{procurementRequestId}";
    public static final String FORM_OPTIONS = BASE + "/form-options";

    private V1() {}
  }

  private BuyerProcurementRequestApi() {}
}
