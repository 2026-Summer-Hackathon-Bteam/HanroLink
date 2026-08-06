package com.hanrolink.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.enums.AccountRole;
import com.hanrolink.account.enums.BusinessUserAccountRegistrationApiStatus;
import com.hanrolink.account.enums.BusinessUserAccountReviewStatus;
import com.hanrolink.account.enums.BusinessUserAccountRole;
import com.hanrolink.account.enums.JwtAccountRole;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.response.CurrentAccountGetResponse;

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
    BusinessUserAccount businessUserAccount = mock(BusinessUserAccount.class);

    when(businessUserAccount.getRole())
      .thenReturn(BusinessUserAccountRole.BUYER);

    when(businessUserAccount.getReviewStatus())
      .thenReturn(BusinessUserAccountReviewStatus.APPROVED);

    when(
      businessUserAccountRepository
        .findByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT)
    ).thenReturn(Optional.of(businessUserAccount));

    CurrentAccountGetResponse response =
      currentAccountService.get(
        null,
        IDENTITY_PROVIDER_SUBJECT
      );

    assertEquals(
      new CurrentAccountGetResponse(
        AccountRole.BUYER,
        BusinessUserAccountRegistrationApiStatus.APPROVED
      ),
      response
    );

    verify(businessUserAccountRepository)
      .findByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT);
  }

  @Test
  void get_shouldReturnNotSubmitted_whenBusinessUserAccountDoesNotExist() {
    when(
      businessUserAccountRepository
        .findByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT)
    ).thenReturn(Optional.empty());

    CurrentAccountGetResponse response =
      currentAccountService.get(
        null,
        IDENTITY_PROVIDER_SUBJECT
      );

    assertEquals(
      new CurrentAccountGetResponse(
        null,
        BusinessUserAccountRegistrationApiStatus.NOT_SUBMITTED
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
        AccountRole.ADMIN,
        null
      ),
      response
    );

    verifyNoInteractions(businessUserAccountRepository);
  }
}
