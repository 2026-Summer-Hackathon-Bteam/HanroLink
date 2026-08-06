package com.hanrolink.account.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.enums.AccountRole;
import com.hanrolink.account.enums.BusinessUserAccountRegistrationApiStatus;
import com.hanrolink.account.enums.BusinessUserAccountReviewStatus;
import com.hanrolink.account.enums.BusinessUserAccountRole;
import com.hanrolink.account.enums.JwtAccountRole;
import com.hanrolink.account.exception.UnsupportedJwtAccountRoleException;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.response.CurrentAccountGetResponse;

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
    if (authenticatedAccountRole == JwtAccountRole.ADMIN) {
      return new CurrentAccountGetResponse(
        AccountRole.ADMIN,
        null
      );
    }

    if (authenticatedAccountRole != null) {
      throw new UnsupportedJwtAccountRoleException();
    }

    Optional<BusinessUserAccount> optionalBusinessUserAccount =
      businessUserAccountRepository
        .findByIdentityProviderSubject(identityProviderSubject);

    if (optionalBusinessUserAccount.isEmpty()) {
      return new CurrentAccountGetResponse(
        null,
        BusinessUserAccountRegistrationApiStatus.NOT_SUBMITTED
      );
    }

    BusinessUserAccount businessUserAccount =
      optionalBusinessUserAccount.orElseThrow();

    return new CurrentAccountGetResponse(
      accountRoleOf(businessUserAccount.getRole()),
      registrationStatusOf(
        businessUserAccount.getReviewStatus()
      )
    );
  }

  private AccountRole accountRoleOf(
    BusinessUserAccountRole role
  ) {
    return switch (role) {
      case SUPPLIER -> AccountRole.SUPPLIER;
      case BUYER -> AccountRole.BUYER;
    };
  }

  private BusinessUserAccountRegistrationApiStatus registrationStatusOf(
    BusinessUserAccountReviewStatus reviewStatus
  ) {
    return switch (reviewStatus) {
      case PENDING ->
        BusinessUserAccountRegistrationApiStatus.PENDING;
      case APPROVED ->
        BusinessUserAccountRegistrationApiStatus.APPROVED;
    };
  }
}
