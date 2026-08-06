package com.hanrolink.onboarding.service;

import org.springframework.stereotype.Service;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.business.entity.Business;
import com.hanrolink.business.repository.BusinessRepository;
import com.hanrolink.onboarding.exception.OnboardingAlreadyExistsException;
import com.hanrolink.onboarding.request.OnboardingCreateRequest;

import jakarta.transaction.Transactional;

@Service
public class OnboardingService {

  private final BusinessRepository businessRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  OnboardingService(
    BusinessRepository businessRepository,
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.businessRepository = businessRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  /**
   * 初期登録情報を新規作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param email 検証済みのメールアドレス
   * @param request 初期登録の入力情報
   */
  @Transactional
  public void create(
    String identityProviderSubject,
    String email,
    OnboardingCreateRequest request
  ) {
    if (businessUserAccountRepository.existsByIdentityProviderSubject(identityProviderSubject)) {
      throw new OnboardingAlreadyExistsException();
    }

    Business business = new Business(
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
      request.businessUserAccount().role(),
      request.businessUserAccount().lastName(),
      request.businessUserAccount().firstName(),
      request.businessUserAccount().lastNameKana(),
      request.businessUserAccount().firstNameKana(),
      request.businessUserAccount().phoneNumber(),
      email
    );

    businessUserAccountRepository.save(businessUserAccount);
  }
}
