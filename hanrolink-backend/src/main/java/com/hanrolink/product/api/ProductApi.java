package com.hanrolink.product.api;

import com.hanrolink.web.api.ApiPath;

public final class ProductApi {

  public static final class V1 {

    public static final String PUBLIC_BASE = ApiPath.API_V1 + "/public/products";
    public static final String BASE = ApiPath.API_V1 + "/products";
    public static final String MINE = ApiPath.API_V1 + "/me/products";
    public static final String BY_ID = BASE + "/{productId}";
    public static final String VISIBILITY = BASE + "/{productId}/visibility";
    public static final String FORM_OPTIONS = BASE + "/form-options";
    public static final String SEARCH_OPTIONS = BASE + "/search-options";

    private V1() {}
  }

  private ProductApi() {}
}
