package com.hanrolink.security.authentication;

import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Profile("cognito")
@Component
public class AuthenticatedUserProvider {

  private final CognitoUserInfoClient cognitoUserInfoClient;

  public AuthenticatedUserProvider(
    CognitoUserInfoClient cognitoUserInfoClient
  ) {
    this.cognitoUserInfoClient = cognitoUserInfoClient;
  }

  public AuthenticatedUser get(
    String expectedSubject,
    String accessToken
  ) {
    AuthenticatedUser authenticatedUser =
      cognitoUserInfoClient.get(accessToken);

    if (authenticatedUser == null
      || !expectedSubject.equals(authenticatedUser.sub())
      || authenticatedUser.email() == null
      || authenticatedUser.email().isBlank()) {
        throw new AccessDeniedException("アクセスが拒否されました");
      }

    return authenticatedUser;
  }
}
