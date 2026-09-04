package com.hanrolink.product.response;

import java.util.List;
import java.util.Objects;

import com.hanrolink.pagination.response.component.PaginationResponse;
import com.hanrolink.product.response.component.ProductSearchResultResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSearchResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductSearchResultResponse> products,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  PaginationResponse pagination
) {
  public ProductSearchResponse {
    Objects.requireNonNull(
      products,
      "ProductSearchResponse.products must not be null"
    );

    Objects.requireNonNull(
      pagination,
      "ProductSearchResponse.pagination must not be null"
    );
  }
}
