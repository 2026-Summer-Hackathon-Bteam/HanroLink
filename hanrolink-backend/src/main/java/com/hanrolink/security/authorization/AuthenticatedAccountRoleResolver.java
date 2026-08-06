package com.hanrolink.security.authorization;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.hanrolink.account.enums.JwtAccountRole;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticatedAccountRoleResolver {

  private static final String GROUPS_CLAIM = "cognito:groups";

  public JwtAccountRole resolve(Jwt jwt) {
    List<String> groups = jwt.getClaimAsStringList(GROUPS_CLAIM);

    if (groups == null) {
      return null;
    }

    List<JwtAccountRole> roles =
      Arrays.stream(JwtAccountRole.values())
        .filter(role ->
          groups.contains(role.name())
        )
        .toList();

    if (roles.size() > 1) {
      log.warn(
        "event=multiple_account_roles, "
          + "subject={}, issuer={}, roles={}, issuedAt={}",
          jwt.getSubject(),
          jwt.getIssuer(),
          roles,
          jwt.getIssuedAt()
      );

      throw new AccessDeniedException(
        "アクセスが拒否されました"
      );
    }

    return roles.isEmpty()
      ? null
      : roles.get(0);
  }
}
