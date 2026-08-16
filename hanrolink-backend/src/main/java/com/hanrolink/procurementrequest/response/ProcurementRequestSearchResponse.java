package com.hanrolink.procurementrequest.response;

import java.util.List;
import java.util.Objects;

import com.hanrolink.pagination.response.component.PaginationResponse;
import com.hanrolink.procurementrequest.response.component.ProcurementRequestSearchResultResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestSearchResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProcurementRequestSearchResultResponse> procurementRequests,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  PaginationResponse pagination
) {
  public ProcurementRequestSearchResponse {
    Objects.requireNonNull(
      procurementRequests,
      "ProcurementRequestSearchResponse.procurementRequests must not be null"
    );

    Objects.requireNonNull(
      pagination,
      "ProcurementRequestSearchResponse.pagination must not be null"
    );
  }
}
