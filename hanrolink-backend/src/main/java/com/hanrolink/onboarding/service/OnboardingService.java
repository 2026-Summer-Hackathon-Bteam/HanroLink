package com.hanrolink.onboarding.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.business.entity.Business;
import com.hanrolink.business.repository.BusinessRepository;
import com.hanrolink.onboarding.exception.OnboardingAlreadyExistsException;
import com.hanrolink.onboarding.request.OnboardingCreateRequest;
import com.hanrolink.onboarding.response.OnboardingGetResponse;
import com.hanrolink.security.authentication.AuthenticatedUser;
import com.hanrolink.security.authentication.AuthenticatedUserProvider;

@Profile("cognito")
@Service
public class OnboardingService {

  private final AuthenticatedUserProvider authenticatedUserProvider;

  private final BusinessRepository businessRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  public OnboardingService(
    AuthenticatedUserProvider authenticatedUserProvider,
    BusinessRepository businessRepository,
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.authenticatedUserProvider = authenticatedUserProvider;
    this.businessRepository = businessRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  /**
   * 初期登録に必要な情報を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param accessToken 認証済みユーザーのアクセストークン
   * @return 初期登録に必要な情報
   */
  public OnboardingGetResponse get(
    String identityProviderSubject,
    String accessToken
  ) {
    AuthenticatedUser currentUser =
      authenticatedUserProvider.get(
        identityProviderSubject,
        accessToken
      );

    return new OnboardingGetResponse(currentUser.email());
  }

  /**
   * 初期登録情報を新規作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param accessToken 認証済みユーザーのアクセストークン
   * @param request 初期登録の入力情報
   */
  @Transactional
  public void create(
    String identityProviderSubject,
    String accessToken,
    OnboardingCreateRequest request
  ) {
    if (businessUserAccountRepository.existsByIdentityProviderSubject(identityProviderSubject)) {
      throw new OnboardingAlreadyExistsException();
    }

    AuthenticatedUser currentUser =
      authenticatedUserProvider.get(
        identityProviderSubject,
        accessToken
      );

    Business business = new Business(
      request.business().role(),
      request.business().name(),
      request.business().nameKana(),
      request.business().websiteUrl(),
      request.business().addressPostalCode(),
      request.business().addressPrefecture(),
      request.business().addressMunicipalityStreet(),
      request.business().addressBuilding(),
      request.business().phoneNumber()
    );

    Business savedBusiness = businessRepository.save(business);

    BusinessUserAccount businessUserAccount = new BusinessUserAccount(
      savedBusiness.getId(),
      identityProviderSubject,
      request.businessUserAccount().lastName(),
      request.businessUserAccount().firstName(),
      request.businessUserAccount().lastNameKana(),
      request.businessUserAccount().firstNameKana(),
      request.businessUserAccount().phoneNumber(),
      currentUser.email()
    );

    businessUserAccountRepository.save(businessUserAccount);
  }
}
