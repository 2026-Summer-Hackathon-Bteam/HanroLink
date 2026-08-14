package com.hanrolink.businessuseraccount.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.hanrolink.account.repository.BusinessUserAccountRepository;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(
  replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql({
  "/test-business.sql",
  "/test-business-user-account.sql"
})
class BusinessUserAccountRepositoryTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer POSTGRES =
    new PostgreSQLContainer("postgres:17.7");

  @Autowired
  private BusinessUserAccountRepository businessUserAccountRepository;

  @Test
  void findBusinessNameByIdentityProviderSubject_shouldReturnBusinessName() {
    String identityProviderSubject = "00000000-0000-0000-0000-000000000001";

    String businessName =
      businessUserAccountRepository
        .findBusinessNameByIdentityProviderSubject(
          identityProviderSubject
        )
        .orElseThrow();

    assertEquals("テスト株式会社", businessName);
  }
}
