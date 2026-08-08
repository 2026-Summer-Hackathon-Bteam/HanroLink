package com.hanrolink.businessapproval.integration;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.hanrolink.businessapproval.api.BusinessApprovalApi;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Sql({
  "/test-business.sql",
  "/test-business-user-account.sql"
})
@Transactional
class ApproveIntegrationTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17.7");

  @Autowired
  private MockMvc mockMvc;

  @Test
  void approve_shouldSucceed_whenValidRequest() throws Exception {
    UUID businessUserAccountId =
      UUID.fromString(
        "00000000-0000-0000-0000-000000000001"
      );

    mockMvc.perform(patch(BusinessApprovalApi.V1.APPROVE, businessUserAccountId)
      .with(
        jwt()
          .jwt(jwt ->
            jwt
              .subject("admin-subject")
              .claim(
                "cognito:groups",
                List.of("ADMIN")
              )
          )
          .authorities(
            new SimpleGrantedAuthority("ROLE_ADMIN")
          )
        )
      )
      .andExpect(status().isNoContent());
  }
}
