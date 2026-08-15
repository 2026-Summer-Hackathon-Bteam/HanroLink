package com.hanrolink.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.repository.projection.BusinessUserAccountAuthorizationProjection;
import com.hanrolink.account.response.CurrentAccountGetResponse;
import com.hanrolink.business.enums.BusinessRegistrationApiStatus;
import com.hanrolink.business.enums.BusinessReviewStatus;
import com.hanrolink.business.enums.BusinessRole;
import com.hanrolink.security.authorization.enums.ApplicationRole;
import com.hanrolink.security.authorization.enums.JwtAccountRole;

@ExtendWith(MockitoExtension.class)
class CurrentAccountServiceTest {

  private static final String IDENTITY_PROVIDER_SUBJECT =
    "test-identity-provider-subject";

  @Mock
  private BusinessUserAccountRepository businessUserAccountRepository;

  @InjectMocks
  private CurrentAccountService currentAccountService;

  @Test
  void get_shouldReturnApprovedBuyer_whenBusinessUserAccountExists() {
    BusinessUserAccountAuthorizationProjection businessUserAccountAuthorization =
      new BusinessUserAccountAuthorizationProjection(
        BusinessRole.BUYER,
        BusinessReviewStatus.APPROVED
      );

    when(
      businessUserAccountRepository
        .findAuthorizationByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT)
    ).thenReturn(Optional.of(businessUserAccountAuthorization));

    CurrentAccountGetResponse response =
      currentAccountService.get(
        null,
        IDENTITY_PROVIDER_SUBJECT
      );

    assertEquals(
      new CurrentAccountGetResponse(
        ApplicationRole.BUYER,
        BusinessRegistrationApiStatus.APPROVED
      ),
      response
    );

    verify(businessUserAccountRepository)
      .findAuthorizationByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT);
  }

  @Test
  void get_shouldReturnNotSubmitted_whenBusinessUserAccountDoesNotExist() {
    when(
      businessUserAccountRepository
        .findAuthorizationByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT)
    ).thenReturn(Optional.empty());

    CurrentAccountGetResponse response =
      currentAccountService.get(
        null,
        IDENTITY_PROVIDER_SUBJECT
      );

    assertEquals(
      new CurrentAccountGetResponse(
        null,
        BusinessRegistrationApiStatus.NOT_SUBMITTED
      ),
      response
    );
  }

  @Test
  void get_shouldReturnAdmin_withoutAccessingRepository() {
    CurrentAccountGetResponse response =
      currentAccountService.get(
        JwtAccountRole.ADMIN,
        IDENTITY_PROVIDER_SUBJECT
      );

    assertEquals(
      new CurrentAccountGetResponse(
        ApplicationRole.ADMIN,
        null
      ),
      response
    );

    verifyNoInteractions(businessUserAccountRepository);
  }
}
