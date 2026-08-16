package com.hanrolink.procurementrequest.service;

import java.time.Instant;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.exception.UnsupportedJwtAccountRoleException;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.repository.projection.BusinessUserAccountAccessScopeProjection;
import com.hanrolink.business.enums.BusinessRole;
import com.hanrolink.negotiationrequest.policy.NegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.policy.ProcurementNegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.repository.ProcurementNegotiationRequestRepository;
import com.hanrolink.procurementrequest.policy.MonthlyProcurementQuantityPolicy;
import com.hanrolink.procurementrequest.repository.MonthlyProcurementQuantityRepository;
import com.hanrolink.procurementrequest.repository.ProcurementRequestRepository;
import com.hanrolink.procurementrequest.repository.ProcurementRequestStorageTypeRepository;
import com.hanrolink.procurementrequest.repository.projection.ProcurementRequestDetailProjection;
import com.hanrolink.procurementrequest.response.ProcurementRequestDetailResponse;
import com.hanrolink.procurementrequest.response.component.MonthlyProcurementQuantityResponse;
import com.hanrolink.procurementrequest.response.component.ProcurementRequestBuyerResponse;
import com.hanrolink.procurementrequest.response.component.ProcurementRequestPermissionsResponse;
import com.hanrolink.product.response.component.StorageTypeResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryResponse;
import com.hanrolink.security.authorization.enums.ApplicationRole;
import com.hanrolink.security.authorization.enums.JwtAccountRole;

@Service
public class ProcurementRequestReadService {

  private final ProcurementRequestRepository procurementRequestRepository;

  private final ProcurementRequestStorageTypeRepository procurementRequestStorageTypeRepository;

  private final MonthlyProcurementQuantityRepository monthlyProcurementQuantityRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository;

  public ProcurementRequestReadService(
    ProcurementRequestRepository procurementRequestRepository,
    ProcurementRequestStorageTypeRepository procurementRequestStorageTypeRepository,
    MonthlyProcurementQuantityRepository monthlyProcurementQuantityRepository,
    BusinessUserAccountRepository businessUserAccountRepository,
    ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository
  ) {
    this.procurementRequestRepository = procurementRequestRepository;
    this.procurementRequestStorageTypeRepository = procurementRequestStorageTypeRepository;
    this.monthlyProcurementQuantityRepository = monthlyProcurementQuantityRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.procurementNegotiationRequestRepository = procurementNegotiationRequestRepository;
  }

  /**
   * 募集詳細情報を取得する
   * @param authenticatedJwtAccountRole JWTから取得したアカウントロール
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param procurementRequestPublicId 取得対象の募集の公開識別子
   * @return 募集詳細情報
   */
  @Transactional(readOnly = true)
  public ProcurementRequestDetailResponse getDetail(
    JwtAccountRole authenticatedJwtAccountRole,
    String identityProviderSubject,
    UUID procurementRequestPublicId
  ) {
    // 認証情報に基づく商品詳細の閲覧者情報の取得
    ProcurementRequestViewer viewer = resolveViewer(
      authenticatedJwtAccountRole,
      identityProviderSubject
    );

    // 募集詳細の表示に必要な基本情報と関連情報の取得
    ProcurementRequestDetailProjection procurementRequest =
      procurementRequestRepository
        .findDetailByPublicId(procurementRequestPublicId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // Buyerの場合、他事業者が登録した募集情報へのアクセスを拒否
    if (viewer.role() == ApplicationRole.BUYER
      && !Objects.equals(viewer.businessId, procurementRequest.buyerBusinessId())
    ) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    List<StorageTypeResponse> storageTypes =
      procurementRequestStorageTypeRepository
        .findStorageTypesByProcurementRequestId(procurementRequest.id())
        .stream()
        .map(storageType ->
          new StorageTypeResponse(
            storageType,
            storageType.getDisplayName()
          )
        )
        .toList();

    List<MonthlyProcurementQuantityResponse> monthlyProcurementQuantities =
      monthlyProcurementQuantityRepository
        .findLatestListByProcurementRequestId(
          procurementRequest.id(),
          Pageable.ofSize(MonthlyProcurementQuantityPolicy.TARGET_MONTH_COUNT)
        )
        .stream()
        .sorted(
          Comparator.comparing(
            monthlyProcurementQuantity ->
              monthlyProcurementQuantity.targetMonth()
          )
        )
        .map(monthlyProcurementQuantity ->
          new MonthlyProcurementQuantityResponse(
            YearMonth.from(
              monthlyProcurementQuantity.targetMonth()
            ),
            monthlyProcurementQuantity.desiredQuantity()
          )
        )
        .toList();

    // 閲覧者に応じた操作権限と商談申請状態の判定
    boolean canManage =
      viewer.businessId() != null
      && viewer.businessId().equals(procurementRequest.buyerBusinessId());

    boolean canCreateNegotiationRequest = false;
    boolean hasMyActiveNegotiationRequest = false;

    if (viewer.role() == ApplicationRole.SUPPLIER) {
      Instant activeSince =
        Instant.now().minus(
          NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
          ChronoUnit.DAYS
        );

      long activeNegotiationRequestCount =
        procurementNegotiationRequestRepository
          .countActiveBySupplierAccountId(
            viewer.businessUserAccountId(),
            activeSince
          );

      canCreateNegotiationRequest =
        activeNegotiationRequestCount < ProcurementNegotiationRequestPolicy.MAX_ACTIVE_REQUEST_COUNT;

      hasMyActiveNegotiationRequest =
        procurementNegotiationRequestRepository
          .existsActiveByProcurementRequestIdAndSupplierAccountId(
            procurementRequest.id(),
            viewer.businessUserAccountId(),
            activeSince
          );
    }

    return new ProcurementRequestDetailResponse(
      procurementRequestPublicId,
      procurementRequest.title(),
      procurementRequest.description(),
      new ProcurementRequestBuyerResponse(
        procurementRequest.buyerBusinessPublicId(),
        procurementRequest.buyerBusinessName()
      ),
      new ProductCategoryResponse(
        procurementRequest.productCategoryId(),
        procurementRequest.productCategoryName()
      ),
      procurementRequest.requiredTradeTerms(),
      procurementRequest.desiredUnitPrice(),
      procurementRequest.deliveryShelfLifeDays(),
      storageTypes,
      monthlyProcurementQuantities,
      new ProcurementRequestPermissionsResponse(
        canManage,
        canCreateNegotiationRequest
      ),
      hasMyActiveNegotiationRequest
    );
  }

  private ProcurementRequestViewer resolveViewer(
    JwtAccountRole authenticatedJwtAccountRole,
    String identityProviderSubject
  ) {
    if (authenticatedJwtAccountRole == JwtAccountRole.ADMIN) {
      return new ProcurementRequestViewer(
        null,
        null,
        ApplicationRole.ADMIN
      );
    }

    if (authenticatedJwtAccountRole != null) {
      throw new UnsupportedJwtAccountRoleException();
    }

    BusinessUserAccountAccessScopeProjection viewerAccessScope =
      businessUserAccountRepository
        .findAccessScopeByIdentityProviderSubject(
          identityProviderSubject
        )
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new ProcurementRequestViewer(
      viewerAccessScope.businessUserAccountId(),
      viewerAccessScope.businessId(),
      applicationRoleOf(viewerAccessScope.businessRole())
    );
  }

  private ApplicationRole applicationRoleOf(
    BusinessRole role
  ) {
    return switch (role) {
      case SUPPLIER -> ApplicationRole.SUPPLIER;
      case BUYER -> ApplicationRole.BUYER;
    };
  }

  private record ProcurementRequestViewer(
    Long businessUserAccountId,
    Long businessId,
    ApplicationRole role
  ) {}
}
