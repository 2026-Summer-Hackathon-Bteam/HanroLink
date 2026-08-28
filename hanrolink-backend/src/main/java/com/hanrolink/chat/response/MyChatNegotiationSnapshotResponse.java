package com.hanrolink.chat.response;

import java.util.Objects;
import java.util.Set;

import com.hanrolink.chat.enums.NegotiationTargetType;
import com.hanrolink.chat.enums.ProcurementRequestSnapshotField;
import com.hanrolink.chat.enums.ProductSnapshotField;
import com.hanrolink.chat.response.component.ProcurementRequestSnapshotResponse;
import com.hanrolink.chat.response.component.ProductSnapshotResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatNegotiationSnapshotResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationTargetType negotiationTargetType,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Set<ProductSnapshotField> productChangedFields,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductSnapshotResponse requestedProductSnapshot,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductSnapshotResponse acceptedProductSnapshot,

  Set<ProcurementRequestSnapshotField> procurementRequestChangedFields,

  ProcurementRequestSnapshotResponse requestedProcurementRequestSnapshot,

  ProcurementRequestSnapshotResponse acceptedProcurementRequestSnapshot
) {
  public MyChatNegotiationSnapshotResponse {
    Objects.requireNonNull(
      negotiationTargetType,
      "MyChatNegotiationSnapshotResponse.negotiationTargetType must not be null"
    );

    Objects.requireNonNull(
      productChangedFields,
      "MyChatNegotiationSnapshotResponse.productChangedFields must not be null"
    );

    Objects.requireNonNull(
      requestedProductSnapshot,
      "MyChatNegotiationSnapshotResponse.requestedProductSnapshot must not be null"
    );

    Objects.requireNonNull(
      acceptedProductSnapshot,
      "MyChatNegotiationSnapshotResponse.acceptedProductSnapshot must not be null"
    );
  }
}
