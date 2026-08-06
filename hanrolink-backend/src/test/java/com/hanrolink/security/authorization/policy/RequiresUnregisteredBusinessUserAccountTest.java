package com.hanrolink.security.authorization.policy;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(
  RequiresUnregisteredBusinessUserAccountTest.TestConfiguration.class
)
class RequiresUnregisteredBusinessUserAccountTest {

  @Autowired
  private SecuredTarget securedTarget;

  @Test
  @WithMockUser(
    authorities = "UNREGISTERED_BUSINESS_USER_ACCOUNT"
  )
  void allowsAccessForUnregisteredBusinessUserAccount() {
    assertDoesNotThrow(securedTarget::execute);
  }

  @Test
  @WithMockUser(authorities = "ROLE_ADMIN")
  void deniesAccessForAdmin() {
    assertThrows(
      AccessDeniedException.class,
      securedTarget::execute
    );
  }

  @Configuration(proxyBeanMethods = false)
  @EnableMethodSecurity
  static class TestConfiguration {

    @Bean
    SecuredTarget securedTarget() {
      return new SecuredTarget();
    }
  }

  static class SecuredTarget {

    @RequiresUnregisteredBusinessUserAccount
    public void execute() {}
  }
}
