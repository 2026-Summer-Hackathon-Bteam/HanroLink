package com.hanrolink.business.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.repository.projection.BusinessProfileAccessProjection;
import com.hanrolink.business.entity.Business;
import com.hanrolink.business.enums.BusinessRole;
import com.hanrolink.business.repository.BusinessRepository;
import com.hanrolink.business.response.BuyerProfileGetResponse;

@Service
public class BuyerBusinessService {

  private final BusinessRepository businessRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  public BuyerBusinessService(
    BusinessRepository businessRepository,
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.businessRepository = businessRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  /**
   * 指定されたバイヤーのプロフィール情報を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param businessPublicId 取得対象事業者の公開識別子
   * @return 取得対象のバイヤープロフィール
   */
  @Transactional(readOnly = true)
  public BuyerProfileGetResponse get(
    String identityProviderSubject,
    UUID businessPublicId
  ) {
    checkBuyerAccess(identityProviderSubject, businessPublicId);

    Business targetBusiness =
      businessRepository
        .findApprovedByPublicIdAndRole(
          businessPublicId,
          BusinessRole.BUYER
        )
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new BuyerProfileGetResponse(
      targetBusiness.getName(),
      targetBusiness.getAddressPrefecture(),
      targetBusiness.getAddressMunicipalityStreet(),
      targetBusiness.getAddressBuilding(),
      targetBusiness.getWebsiteUrl()
    );
  }

  private void checkBuyerAccess(
    String identityProviderSubject,
    UUID businessPublicId
  ) {
    Optional<BusinessProfileAccessProjection> optionalViewerAccess =
      businessUserAccountRepository
        .findBusinessProfileAccessByIdentityProviderSubject(identityProviderSubject);

    // DBにアカウントがない場合はAdmin
    if (optionalViewerAccess.isEmpty()) {
      return;
    }

    BusinessProfileAccessProjection viewerAccess =
      optionalViewerAccess.orElseThrow();

    if (viewerAccess.businessRole() == BusinessRole.SUPPLIER) {
      return;
    }

    // Buyer本人なら取得可能
    if (
      viewerAccess.businessPublicId()
        .equals(businessPublicId)
    ) {
      return;
    }

    throw new AccessDeniedException("アクセスが拒否されました");
  }
}
