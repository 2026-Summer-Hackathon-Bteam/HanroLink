package com.hanrolink.pagination.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record PaginationResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Integer page,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Integer pageSize,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long totalCount,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Integer totalPages
) {
  public PaginationResponse {
    Objects.requireNonNull(
      page,
      "PaginationResponse.page must not be null"
    );

    Objects.requireNonNull(
      pageSize,
      "PaginationResponse.pageSize must not be null"
    );

    Objects.requireNonNull(
      totalCount,
      "PaginationResponse.totalCount must not be null"
    );

    Objects.requireNonNull(
      totalPages,
      "PaginationResponse.totalPages must not be null"
    );
  }
}
