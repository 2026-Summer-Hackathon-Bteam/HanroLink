package com.hanrolink.procurementrequest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.procurementrequest.api.ProcurementRequestApi;
import com.hanrolink.procurementrequest.request.BuyerProcurementRequestCreateRequest;
import com.hanrolink.procurementrequest.request.BuyerProcurementRequestUpdateRequest;
import com.hanrolink.procurementrequest.response.BuyerProcurementRequestCreateResponse;
import com.hanrolink.procurementrequest.response.BuyerProcurementRequestListResponse;
import com.hanrolink.procurementrequest.service.BuyerProcurementRequestManagementService;
import com.hanrolink.security.authorization.policy.RequiresApprovedBuyer;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

/**
 * 認証中のBuyerが自社の募集情報を管理するためのController。
 * 他のBuyerが登録した募集情報は操作対象に含まない。
 */
@RestController
public class BuyerProcurementRequestManagementController {

  private final BuyerProcurementRequestManagementService buyerProcurementRequestManagementService;

  public BuyerProcurementRequestManagementController(
    BuyerProcurementRequestManagementService buyerProcurementRequestManagementService
  ) {
    this.buyerProcurementRequestManagementService = buyerProcurementRequestManagementService;
  }

  /**
   * 募集情報を新規作成を受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @param request 募集の入力情報
   * @return 募集の作成結果
   */
  @RequiresApprovedBuyer
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(ProcurementRequestApi.V1.BASE)
  public ResponseEntity<BuyerProcurementRequestCreateResponse> create(
    @AuthenticationPrincipal Jwt jwt,
    @Valid @RequestBody BuyerProcurementRequestCreateRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
      buyerProcurementRequestManagementService.create(
        jwt.getSubject(),
        request
      )
    );
  }

  // 認証中のBuyerアカウントが登録した募集のみ取得可能
  @GetMapping(ProcurementRequestApi.V1.MINE)
  public ResponseEntity<List<BuyerProcurementRequestListResponse>> list() {

    // TODO: Serviceから募集情報リストを取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  /**
   * 募集情報の更新を受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @param procurementRequestId 更新対象の募集の公開識別子
   * @param request 募集の更新情報
   * @return 募集の更新結果
   */
  @RequiresApprovedBuyer
  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @PutMapping(ProcurementRequestApi.V1.BY_ID)
  public ResponseEntity<Void> update(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID procurementRequestId,
    @Valid @RequestBody BuyerProcurementRequestUpdateRequest request
  ) {
    buyerProcurementRequestManagementService.update(
      jwt.getSubject(),
      procurementRequestId,
      request
    );

    return ResponseEntity.noContent().build();
  }

  // 募集を登録したBuyerアカウントのみ利用可能
  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @DeleteMapping(ProcurementRequestApi.V1.BY_ID)
  public ResponseEntity<Void> delete(
    @PathVariable UUID procurementRequestId
  ) {

    // TODO: Serviceで登録元Buyerの募集情報を削除し、204 No Contentを返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
