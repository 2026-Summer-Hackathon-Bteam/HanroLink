package com.hanrolink.procurementrequest.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.procurementrequest.api.ProcurementRequestApi;
import com.hanrolink.procurementrequest.request.ProcurementRequestSearchRequest;
import com.hanrolink.procurementrequest.response.ProcurementRequestDetailResponse;
import com.hanrolink.procurementrequest.response.ProcurementRequestSearchResponse;
import com.hanrolink.procurementrequest.service.ProcurementRequestReadService;
import com.hanrolink.security.authorization.AuthenticatedAccountRoleResolver;
import com.hanrolink.security.authorization.enums.JwtAccountRole;
import com.hanrolink.security.authorization.policy.RequiresAdminOrApprovedBusiness;

import jakarta.validation.Valid;

/**
 * 募集情報の閲覧API。
 * Buyerによる自社募集の管理は {@link BuyerProcurementRequestManagementController}で扱う。
 */
@RestController
public class ProcurementRequestReadController {

  private final ProcurementRequestReadService procurementRequestReadService;

  private final AuthenticatedAccountRoleResolver authenticatedAccountRoleResolver;

  public ProcurementRequestReadController(
    ProcurementRequestReadService procurementRequestReadService,
    AuthenticatedAccountRoleResolver authenticatedAccountRoleResolver
  ) {
    this.procurementRequestReadService = procurementRequestReadService;
    this.authenticatedAccountRoleResolver = authenticatedAccountRoleResolver;
  }

  /**
   * 募集詳細情報を取得することを受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @param procurementRequestId 取得対象の募集の公開識別子
   * @return 募集詳細情報
   */
  @RequiresAdminOrApprovedBusiness
  @GetMapping(ProcurementRequestApi.V1.BY_ID)
  public ResponseEntity<ProcurementRequestDetailResponse> getDetail(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID procurementRequestId
  ) {
    JwtAccountRole authenticatedJwtAccountRole =
      authenticatedAccountRoleResolver.resolve(jwt);

    return ResponseEntity.ok(
      procurementRequestReadService.getDetail(
        authenticatedJwtAccountRole,
        jwt.getSubject(),
        procurementRequestId
      )
    );
  }

  // 管理者、サプライヤー利用可能
  @GetMapping(ProcurementRequestApi.V1.BASE)
  public ResponseEntity<ProcurementRequestSearchResponse> search(
    @Valid
    @ParameterObject
    @ModelAttribute
    ProcurementRequestSearchRequest request
  ) {

    // TODO: Serviceから募集情報リストを取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
