package com.hanrolink.pagination.response.component;

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
) {}
