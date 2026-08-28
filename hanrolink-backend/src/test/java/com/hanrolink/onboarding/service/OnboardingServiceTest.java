package com.hanrolink.onboarding.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.business.entity.Business;
import com.hanrolink.business.enums.BusinessRole;
import com.hanrolink.business.repository.BusinessRepository;
import com.hanrolink.onboarding.exception.OnboardingAlreadyExistsException;
import com.hanrolink.onboarding.request.OnboardingCreateRequest;
import com.hanrolink.security.authentication.AuthenticatedUser;
import com.hanrolink.security.authentication.AuthenticatedUserProvider;

@ExtendWith(MockitoExtension.class)
class OnboardingServiceTest {

  private static final String IDENTITY_PROVIDER_SUBJECT =
    "test-identity-provider-subject";

  private static final String ACCESS_TOKEN =
    "test-access-token";

  @Mock
  private AuthenticatedUserProvider authenticatedUserProvider;

  @Mock
  private BusinessRepository businessRepository;

  @Mock
  private BusinessUserAccountRepository businessUserAccountRepository;

  @InjectMocks
  private OnboardingService onboardingService;

  @Test
  void create_shouldSaveBusinessAndBusinessUserAccount_whenAccountDoesNotExist() {
    String email = "test@example.com";

    OnboardingCreateRequest request = mock(
      OnboardingCreateRequest.class,
      RETURNS_DEEP_STUBS
    );

    when(request.business().role())
      .thenReturn(BusinessRole.BUYER);
    when(request.business().name())
      .thenReturn("テスト株式会社");
    when(request.business().nameKana())
      .thenReturn("テストカブシキガイシャ");
    when(request.business().websiteUrl())
      .thenReturn("https://example.com");
    when(request.business().addressPostalCode())
      .thenReturn("1000001");
    when(request.business().addressPrefecture())
      .thenReturn("東京都");
    when(request.business().addressMunicipalityStreet())
      .thenReturn("千代田区千代田1-1");
    when(request.business().addressBuilding())
      .thenReturn("テストビル");
    when(request.business().phoneNumber())
      .thenReturn("0312345678");

    when(request.businessUserAccount().lastName())
      .thenReturn("山田");
    when(request.businessUserAccount().firstName())
      .thenReturn("太郎");
    when(request.businessUserAccount().lastNameKana())
      .thenReturn("ヤマダ");
    when(request.businessUserAccount().firstNameKana())
      .thenReturn("タロウ");
    when(request.businessUserAccount().phoneNumber())
      .thenReturn("09012345678");

    when(
      businessUserAccountRepository
        .existsByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT)
    ).thenReturn(false);

    when(
      authenticatedUserProvider.get(
        IDENTITY_PROVIDER_SUBJECT,
        ACCESS_TOKEN
      )
    ).thenReturn(
      new AuthenticatedUser(
        IDENTITY_PROVIDER_SUBJECT,
        email
      )
    );

    Business savedBusiness = mock(Business.class);

    when(savedBusiness.getId()).thenReturn(1L);

    when(businessRepository.save(any(Business.class)))
      .thenReturn(savedBusiness);

    onboardingService.create(
      IDENTITY_PROVIDER_SUBJECT,
      ACCESS_TOKEN,
      request
    );

    InOrder inOrder =inOrder(
      businessRepository,
      businessUserAccountRepository
    );

    inOrder.verify(businessRepository).save(any(Business.class));

    inOrder.verify(businessUserAccountRepository)
      .save(any(BusinessUserAccount.class));

    verify(savedBusiness).getId();
  }

  @Test
  void create_shouldThrowException_whenAccountAlreadyExists() {
    OnboardingCreateRequest request = mock(OnboardingCreateRequest.class);

    when(
      businessUserAccountRepository
        .existsByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT)
    ).thenReturn(true);

    assertThrows(
      OnboardingAlreadyExistsException.class,
      () -> onboardingService.create(
        IDENTITY_PROVIDER_SUBJECT,
        "test@example.com",
        request
      )
    );

    verifyNoInteractions(businessRepository);

    verify(
      businessUserAccountRepository,
      never()
    ).save(any());
  }
}
