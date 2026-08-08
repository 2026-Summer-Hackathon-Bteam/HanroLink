package com.hanrolink.businessapproval.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
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
   * 審査待ちの事業者ユーザーアカウント一覧を取得する
   * @return 審査待ちの事業者ユーザーアカウント一覧
   */
  @RequiresAdmin
  @GetMapping(BusinessApprovalApi.V1.PENDING)
  public ResponseEntity<List<AdminBusinessApprovalListResponse>> listPending() {
    return ResponseEntity.ok(
      adminBusinessApprovalService.listPending()
    );
  }

  /**
   * 審査対象の詳細情報を取得する
   * @param businessUserAccountId 事業者ユーザーアカウントの公開識別子
   * @return 審査対象の詳細情報
   */
  @RequiresAdmin
  @GetMapping(BusinessApprovalApi.V1.BY_ID)
  public ResponseEntity<AdminBusinessApprovalDetailResponse> getDetail(
    @PathVariable UUID businessUserAccountId
  ) {
    return ResponseEntity.ok(
      adminBusinessApprovalService.getDetail(businessUserAccountId)
    );
  }

  // 管理者のみ利用可能
  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @PatchMapping(BusinessApprovalApi.V1.APPROVE)
  public ResponseEntity<Void> approve(
    @PathVariable UUID businessUserAccountId
  ) {

    // TODO: 承認待ちの対象アカウントの審査状態をAPPROVEDへ変更し、204 No Contentを返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
