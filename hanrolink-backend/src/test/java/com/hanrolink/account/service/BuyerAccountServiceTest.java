package com.hanrolink.account.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.enums.BusinessUserAccountRole;
import com.hanrolink.account.repository.BusinessUserAccountRepository;

@ExtendWith(MockitoExtension.class)
class BuyerAccountServiceTest {

  private static final String IDENTITY_PROVIDER_SUBJECT =
    "test-identity-provider-subject";

  @Mock
  private BusinessUserAccountRepository businessUserAccountRepository;

  @InjectMocks
  private BuyerAccountService buyerAccountService;

  @Test
  void get_shouldThrowAccessDenied_whenBuyerAccessAnotherAccount() {
    UUID currentBusinessUserAccountId =
      UUID.fromString(
        "00000000-0000-0000-0000-000000000001"
      );

    UUID targetBusinessUserAccountId =
      UUID.fromString(
        "00000000-0000-0000-0000-000000000002"
      );

    BusinessUserAccount currentBuyer =
      mock(BusinessUserAccount.class);

    when(currentBuyer.getRole())
      .thenReturn(BusinessUserAccountRole.BUYER);

    when(currentBuyer.getPublicId())
      .thenReturn(currentBusinessUserAccountId);

    when(
      businessUserAccountRepository
        .findByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT)
    ).thenReturn(Optional.of(currentBuyer));

    assertThrows(
      AccessDeniedException.class,
      () ->
        buyerAccountService.get(
          IDENTITY_PROVIDER_SUBJECT,
          targetBusinessUserAccountId
        )
    );

    verify(businessUserAccountRepository, never())
      .findBusinessByBusinessUserAccountPublicIdAndRole(
        targetBusinessUserAccountId,
        BusinessUserAccountRole.BUYER
      );
  }
}
