package com.hanrolink.infrastructure.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Profile("s3")
@Configuration
public class S3Config {

  @Bean
  S3Presigner s3Presigner(
    @Value("${app.storage.s3.region}")
    String region
  ) {
    return S3Presigner
      .builder()
      .region(Region.of(region))
      .build();
  }

  @Bean
  S3Client s3Client(
    @Value("${app.storage.s3.region}")
    String region
  ) {
    return S3Client
      .builder()
      .region(Region.of(region))
      .build();
  }
}
