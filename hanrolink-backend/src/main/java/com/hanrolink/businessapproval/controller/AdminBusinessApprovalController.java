package com.hanrolink.businessapproval.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.businessapproval.api.BusinessApprovalApi;
import com.hanrolink.businessapproval.response.AdminBusinessApprovalDetailResponse;
import com.hanrolink.businessapproval.response.AdminBusinessApprovalListResponse;
import com.hanrolink.businessapproval.service.AdminBusinessApprovalService;
import com.hanrolink.security.authorization.policy.RequiresAdmin;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class AdminBusinessApprovalController {

  private final AdminBusinessApprovalService adminBusinessApprovalService;

  public AdminBusinessApprovalController(
    AdminBusinessApprovalService adminBusinessApprovalService
  ) {
    this.adminBusinessApprovalService = adminBusinessApprovalService;
  }

  /**
   * 審査待ちの事業者一覧を返す
   * @return 審査待ちの事業者一覧
   */
  @RequiresAdmin
  @GetMapping(BusinessApprovalApi.V1.PENDING)
  public ResponseEntity<List<AdminBusinessApprovalListResponse>> listPending() {
    return ResponseEntity.ok(
      adminBusinessApprovalService.listPending()
    );
  }

  /**
   * 審査対象の詳細情報を返す
   * @param businessId 事業者の公開識別子
   * @return 審査対象の詳細情報
   */
  @RequiresAdmin
  @GetMapping(BusinessApprovalApi.V1.BY_ID)
  public ResponseEntity<AdminBusinessApprovalDetailResponse> getDetail(
    @PathVariable UUID businessId
  ) {
    return ResponseEntity.ok(
      adminBusinessApprovalService.getDetail(businessId)
    );
  }

  /**
   * 審査対象の事業者を承認する
   * @param businessId 事業者の公開識別子
   * @return 承認結果
   */
  @RequiresAdmin
  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @PatchMapping(BusinessApprovalApi.V1.APPROVE)
  public ResponseEntity<Void> approve(
    @PathVariable UUID businessId
  ) {
    adminBusinessApprovalService.approve(businessId);
    return ResponseEntity.noContent().build();
  }
}
