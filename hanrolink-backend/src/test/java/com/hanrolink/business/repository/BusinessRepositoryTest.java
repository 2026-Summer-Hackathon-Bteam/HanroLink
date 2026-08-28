package com.hanrolink.business.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.hanrolink.business.enums.BusinessReviewStatus;
import com.hanrolink.business.repository.projection.AdminBusinessApprovalListProjection;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(
  replace = AutoConfigureTestDatabase.Replace.NONE
)
@Sql({
  "/test-business.sql",
  "/test-business-user-account.sql"
})
class BusinessRepositoryTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer POSTGRES =
    new PostgreSQLContainer("postgres:17.7");

  @Autowired
  private BusinessRepository businessRepository;

  @Test
  void findApprovalListByReviewStatus_shouldReturnPendingBusinesses() {
    List<AdminBusinessApprovalListProjection> responses =
      businessRepository
        .findApprovalListByReviewStatus(
          BusinessReviewStatus.PENDING
        );

    assertEquals(1, responses.size());

    AdminBusinessApprovalListProjection response =
      responses.getFirst();

    assertEquals(
      UUID.fromString("00000000-0000-0000-0000-000000000001"),
      response.businessId()
    );
    assertEquals(
      "テスト株式会社",
      response.businessName()
    );
    assertNotNull(response.createdAt());
  }
}
