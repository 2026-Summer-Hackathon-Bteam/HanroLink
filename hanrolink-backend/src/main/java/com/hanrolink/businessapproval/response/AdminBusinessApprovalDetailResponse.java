package com.hanrolink.businessapproval.response;

import com.hanrolink.businessapproval.response.component.BusinessApprovalAccountResponse;
import com.hanrolink.businessapproval.response.component.BusinessApprovalBusinessResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminBusinessApprovalDetailResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessApprovalAccountResponse businessUserAccount,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessApprovalBusinessResponse business
) {}