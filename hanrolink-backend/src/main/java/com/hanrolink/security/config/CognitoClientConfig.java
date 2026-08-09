package com.hanrolink.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
@Profile("cognito")
public class CognitoClientConfig {

  @Bean
  CognitoIdentityProviderClient cognitoIdentityProviderClient(
    @Value("${app.security.cognito.region}")
    String region
  ) {
    return CognitoIdentityProviderClient
      .builder()
      .region(Region.of(region))
      .build();
  }
}
