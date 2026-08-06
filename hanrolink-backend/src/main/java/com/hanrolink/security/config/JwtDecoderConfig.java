package com.hanrolink.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
@Profile("cognito")
public class JwtDecoderConfig {

  @Bean
  JwtDecoder jwtDecoder(
    @Value("${app.security.cognito.issuer-uri}")
    String issuerUri,

    @Value("${app.security.cognito.client-id}")
    String clientId
  ) {
    NimbusJwtDecoder decoder =
      NimbusJwtDecoder.withIssuerLocation(issuerUri).build();

    OAuth2TokenValidator<Jwt> defaultValidators =
      JwtValidators.createDefaultWithIssuer(issuerUri);

    OAuth2TokenValidator<Jwt> tokenUseValidator =
      new JwtClaimValidator<String>("token_use", "access"::equals);

    OAuth2TokenValidator<Jwt> clientIdValidator =
      new JwtClaimValidator<String>("client_id", clientId::equals);

    OAuth2TokenValidator<Jwt> subjectValidator =
      new JwtClaimValidator<String>("sub", sub -> sub != null && !sub.isBlank());

    OAuth2TokenValidator<Jwt> emailValidator =
      new JwtClaimValidator<String>("email", email -> email != null && !email.isBlank());

    OAuth2TokenValidator<Jwt> validators =
      new DelegatingOAuth2TokenValidator<>(
        defaultValidators,
        tokenUseValidator,
        clientIdValidator,
        subjectValidator,
        emailValidator
      );

    decoder.setJwtValidator(validators);

    return decoder;
  }
}
