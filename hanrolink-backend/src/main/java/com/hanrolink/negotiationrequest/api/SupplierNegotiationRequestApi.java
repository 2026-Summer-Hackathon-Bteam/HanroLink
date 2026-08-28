package com.hanrolink.negotiationrequest.api;

import com.hanrolink.web.api.ApiPath;

public final class SupplierNegotiationRequestApi {

  public static final class V1 {

    private static final String MINE = ApiPath.API_V1 + "/me/supplier";
    public static final String CREATE =
      ApiPath.API_V1 + "/procurement-requests/{procurementRequestId}/negotiation-requests";
    public static final String SELECTABLE_PRODUCTS =
      ApiPath.API_V1 + "/procurement-negotiation-requests/selectable-products";
    public static final String MINE_PROCUREMENT_NEGOTIATION_REQUESTS =
      MINE + "/procurement-negotiation-requests";
    public static final String MINE_PRODUCT_NEGOTIATION_REQUESTS =
      MINE + "/product-negotiation-requests";
    public static final String ACCEPT =
      MINE_PRODUCT_NEGOTIATION_REQUESTS + "/{productNegotiationRequestId}/accept";

    private V1() {}
  }

  private SupplierNegotiationRequestApi() {}
}
