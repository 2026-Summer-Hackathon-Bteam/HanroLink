package com.hanrolink.product.response;

import java.util.List;
import java.util.Objects;

import com.hanrolink.pagination.response.component.PaginationResponse;
import com.hanrolink.product.response.component.ProductSearchListItemResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSearchListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductSearchListItemResponse> products,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  PaginationResponse pagination
) {
  public ProductSearchListResponse {
    Objects.requireNonNull(
      pagination,
      "ProductSearchListResponse.pagination must not be null"
    );
  }
}
