package com.hanrolink.account.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.account.exception.UnsupportedJwtAccountRoleException;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.repository.projection.BusinessUserAccountAuthorizationProjection;
import com.hanrolink.account.response.CurrentAccountGetResponse;
import com.hanrolink.business.enums.BusinessRegistrationApiStatus;
import com.hanrolink.business.enums.BusinessReviewStatus;
import com.hanrolink.business.enums.BusinessRole;
import com.hanrolink.security.authorization.enums.ApplicationRole;
import com.hanrolink.security.authorization.enums.JwtAccountRole;

@Service
public class CurrentAccountService {

  private final BusinessUserAccountRepository businessUserAccountRepository;

  public CurrentAccountService(
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  /**
   * 認証済みユーザーのアカウント状態を取得する
   * @param authenticatedAccountRole JWTから取得したアカウントロール
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @return 認証済みユーザーのアカウント状態
   */
  @Transactional(readOnly = true)
  public CurrentAccountGetResponse get(
    JwtAccountRole authenticatedAccountRole,
    String identityProviderSubject
  ) {
    // Adminユーザー向けレスポンスの返却
    if (authenticatedAccountRole == JwtAccountRole.ADMIN) {
      return new CurrentAccountGetResponse(
        ApplicationRole.ADMIN,
        null
      );
    }

    // Admin以外のJWTロールの拒否
    if (authenticatedAccountRole != null) {
      throw new UnsupportedJwtAccountRoleException();
    }

    // Admin以外は、DBの登録情報からロールと審査状態の取得
    Optional<BusinessUserAccountAuthorizationProjection> optionalBusinessUserAccountAuthorization =
      businessUserAccountRepository
        .findAuthorizationByIdentityProviderSubject(identityProviderSubject);

    if (optionalBusinessUserAccountAuthorization.isEmpty()) {
      return new CurrentAccountGetResponse(
        null,
        BusinessRegistrationApiStatus.NOT_SUBMITTED
      );
    }

    BusinessUserAccountAuthorizationProjection businessUserAccountAuthorization =
      optionalBusinessUserAccountAuthorization.orElseThrow();

    return new CurrentAccountGetResponse(
      applicationRoleOf(businessUserAccountAuthorization.businessRole()),
      registrationStatusOf(
        businessUserAccountAuthorization.businessReviewStatus()
      )
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

  private BusinessRegistrationApiStatus registrationStatusOf(
    BusinessReviewStatus reviewStatus
  ) {
    return switch (reviewStatus) {
      case PENDING ->
        BusinessRegistrationApiStatus.PENDING;
      case APPROVED ->
        BusinessRegistrationApiStatus.APPROVED;
    };
  }
}
