package com.hanrolink.account.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.enums.BusinessUserAccountRole;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.response.BuyerProfileGetResponse;
import com.hanrolink.business.entity.Business;

@Service
public class BuyerAccountService {

  private final BusinessUserAccountRepository businessUserAccountRepository;

  public BuyerAccountService(
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  /**
   * 指定されたバイヤーのプロフィール情報を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param targetBusinessUserAccountId 取得対象アカウントの公開識別子
   * @return 取得対象のバイヤープロフィール
   */
  @Transactional(readOnly = true)
  public BuyerProfileGetResponse get(
    String identityProviderSubject,
    UUID targetBusinessUserAccountId
  ) {
    checkBuyerAccess(identityProviderSubject, targetBusinessUserAccountId);

    Business targetBusiness =
      businessUserAccountRepository
        .findBusinessByBusinessUserAccountPublicIdAndRole(
          targetBusinessUserAccountId,
          BusinessUserAccountRole.BUYER
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
    UUID targetBusinessUserAccountId
  ) {
    Optional<BusinessUserAccount> optionalCurrentBusinessUserAccount =
      businessUserAccountRepository
        .findByIdentityProviderSubject(identityProviderSubject);

    // DBにアカウントがない場合はAdmin
    if (optionalCurrentBusinessUserAccount.isEmpty()) {
      return;
    }

    BusinessUserAccount currentBusinessUserAccount =
      optionalCurrentBusinessUserAccount.orElseThrow();

    if (currentBusinessUserAccount.getRole() == BusinessUserAccountRole.SUPPLIER) {
      return;
    }

    // Buyer本人なら取得可能
    if (
      currentBusinessUserAccount
        .getPublicId()
        .equals(targetBusinessUserAccountId)
    ) {
      return;
    }

    throw new AccessDeniedException("アクセスが拒否されました");
  }
}
