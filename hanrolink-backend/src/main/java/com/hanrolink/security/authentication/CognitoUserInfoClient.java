package com.hanrolink.security.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Profile("cognito")
@Component
public class CognitoUserInfoClient {

  private final RestClient restClient;

  public CognitoUserInfoClient(
    RestClient.Builder restClientBuilder,
    @Value("${app.security.cognito.user-info-uri}")
    String userInfoUri
  ) {
    this.restClient = restClientBuilder.baseUrl(userInfoUri).build();
  }

  public AuthenticatedUser get(
    String accessToken
  ) {
    return restClient
      .get()
      .headers(headers -> headers.setBearerAuth(accessToken))
      .retrieve()
      .body(AuthenticatedUser.class);
  }
}
