package com.hanrolink.negotiationrequest.api;

import com.hanrolink.web.api.ApiPath;

public final class BuyerNegotiationRequestApi {

  public static final class V1 {

    private static final String MINE = ApiPath.API_V1 + "/me/buyer";
    public static final String CREATE =
      ApiPath.API_V1 + "/products/{productId}/negotiation-requests";
    public static final String MINE_PRODUCT_NEGOTIATION_REQUESTS =
      MINE + "/product-negotiation-requests";
    public static final String MINE_PROCUREMENT_NEGOTIATION_REQUESTS =
      MINE + "/procurement-negotiation-requests";
    public static final String ACCEPT =
      MINE_PROCUREMENT_NEGOTIATION_REQUESTS + "/{procurementNegotiationRequestId}/accept";

    private V1() {}
  }

  private BuyerNegotiationRequestApi() {}
}
