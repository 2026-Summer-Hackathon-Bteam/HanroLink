package com.hanrolink.businessapproval.response;

import java.util.Objects;

import com.hanrolink.businessapproval.response.component.BusinessApprovalAccountResponse;
import com.hanrolink.businessapproval.response.component.BusinessApprovalBusinessResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminBusinessApprovalDetailResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessApprovalAccountResponse businessUserAccount,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessApprovalBusinessResponse business
) {
  public AdminBusinessApprovalDetailResponse {
    Objects.requireNonNull(
      businessUserAccount,
      "AdminBusinessApprovalDetailResponse.businessUserAccount must not be null"
    );

    Objects.requireNonNull(
      business,
      "AdminBusinessApprovalDetailResponse.business must not be null"
    );
  }
}