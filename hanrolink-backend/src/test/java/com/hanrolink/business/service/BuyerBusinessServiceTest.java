package com.hanrolink.business.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.repository.projection.BusinessProfileAccessProjection;
import com.hanrolink.business.enums.BusinessReviewStatus;
import com.hanrolink.business.enums.BusinessRole;
import com.hanrolink.business.repository.BusinessRepository;

@ExtendWith(MockitoExtension.class)
class BuyerBusinessServiceTest {

  private static final String IDENTITY_PROVIDER_SUBJECT =
    "test-identity-provider-subject";

  @Mock
  private BusinessRepository businessRepository;

  @Mock
  private BusinessUserAccountRepository businessUserAccountRepository;

  @InjectMocks
  private BuyerBusinessService buyerBusinessService;

  @Test
  void get_shouldThrowAccessDenied_whenBuyerAccessAnotherAccount() {
    UUID currentBusinessId =
      UUID.fromString(
        "00000000-0000-0000-0000-000000000001"
      );

    UUID targetBusinessId =
      UUID.fromString(
        "00000000-0000-0000-0000-000000000002"
      );

    BusinessProfileAccessProjection currentBusinessAccess =
      new BusinessProfileAccessProjection(
        currentBusinessId,
        BusinessRole.BUYER
      );

    when(
      businessUserAccountRepository
        .findBusinessProfileAccessByIdentityProviderSubject(IDENTITY_PROVIDER_SUBJECT)
    ).thenReturn(Optional.of(currentBusinessAccess));

    assertThrows(
      AccessDeniedException.class,
      () ->
        buyerBusinessService.get(
          null,
          IDENTITY_PROVIDER_SUBJECT,
          targetBusinessId
        )
    );

    verify(businessRepository, never())
      .findByPublicIdAndRoleAndReviewStatus(
        targetBusinessId,
        BusinessRole.BUYER,
        BusinessReviewStatus.APPROVED
      );
  }
}
