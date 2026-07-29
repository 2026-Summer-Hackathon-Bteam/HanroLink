package com.hanrolink.procurementrequest.api;

import com.hanrolink.web.api.ApiPath;

public final class ProcurementRequestApi {

  public static final class V1 {

    public static final String BASE = ApiPath.API_V1 + "/procurement-requests";
    public static final String MINE = ApiPath.API_V1 + "/me/procurement-requests";
    public static final String BY_ID = BASE + "/{procurementRequestId}";
    public static final String FORM_OPTIONS = BASE + "/form-options";
    public static final String SEARCH_OPTIONS = BASE + "/search-options";

    private V1() {}
  }

  private ProcurementRequestApi() {}
}
