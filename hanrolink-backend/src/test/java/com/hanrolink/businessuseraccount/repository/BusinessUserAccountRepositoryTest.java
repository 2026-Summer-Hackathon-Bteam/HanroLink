package com.hanrolink.businessuseraccount.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.enums.BusinessUserAccountRole;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.business.entity.Business;
import com.hanrolink.business.repository.BusinessRepository;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(
  replace = AutoConfigureTestDatabase.Replace.NONE
)
class BusinessUserAccountRepositoryTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer POSTGRES =
    new PostgreSQLContainer("postgres:17.7");

  @Autowired
  private BusinessRepository businessRepository;

  @Autowired
  private BusinessUserAccountRepository businessUserAccountRepository;

  @Test
  void findsBusinessNameByIdentityProviderSubject() {
    String identityProviderSubject = "cognito-sub-001";

    Business business = new Business(
      "テスト株式会社",
      "テストカブシキガイシャ",
      "https://example.com",
      "1000001",
      "東京都",
      "千代田区千代田1-1",
      null,
      "0312345678"
    );

    Business savedBusiness =
      businessRepository.saveAndFlush(business);

    BusinessUserAccount businessUserAccount =
      new BusinessUserAccount(
        savedBusiness.getId(),
        identityProviderSubject,
        BusinessUserAccountRole.SUPPLIER,
        "山田",
        "太郎",
        "ヤマダ",
        "タロウ",
        "09012345678",
        "test@example.com"
      );

    businessUserAccountRepository.saveAndFlush(businessUserAccount);

    String businessName =
      businessUserAccountRepository
        .findBusinessNameByIdentityProviderSubject(
          identityProviderSubject
        )
        .orElseThrow();

    assertEquals("テスト株式会社", businessName);
  }
}
