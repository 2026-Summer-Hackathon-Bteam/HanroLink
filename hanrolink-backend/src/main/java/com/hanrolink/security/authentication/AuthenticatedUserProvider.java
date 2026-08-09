package com.hanrolink.security.authentication;

import org.springframework.context.annotation.Profile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserNotFoundException;

@Profile("cognito")
@Component
public class AuthenticatedUserProvider {

  private static final String SUBJECT_ATTRIBUTE = "sub";
  private static final String EMAIL_ATTRIBUTE = "email";

  private final CognitoIdentityProviderClient cognitoIdentityProviderClient;

  public AuthenticatedUserProvider(
    CognitoIdentityProviderClient cognitoIdentityProviderClient
  ) {
    this.cognitoIdentityProviderClient = cognitoIdentityProviderClient;
  }

  public AuthenticatedUser get(
    String expectedSubject,
    String accessToken
  ) {
    GetUserResponse response;

    try {
      response = cognitoIdentityProviderClient.getUser(
        GetUserRequest
          .builder()
          .accessToken(accessToken)
          .build()
      );
    } catch (
      NotAuthorizedException
        | UserNotFoundException exception
    ) {
      throw new AccessDeniedException(
        "アクセスが拒否されました",
        exception
      );
    }

    String subject = attributeValue(response, SUBJECT_ATTRIBUTE);

    String email = attributeValue(response, EMAIL_ATTRIBUTE);

    if (!expectedSubject.equals(subject)
      || email == null
      || email.isBlank()) {
      throw new AccessDeniedException("アクセスが拒否されました");
    }

    return new AuthenticatedUser(subject, email);
  }

  private String attributeValue(
    GetUserResponse response,
    String attributeName
  ) {
    return response
      .userAttributes()
      .stream()
      .filter(attribute ->
        attributeName.equals(attribute.name())
      )
      .map(attribute -> attribute.value())
      .findFirst()
      .orElse(null);
  }
}
