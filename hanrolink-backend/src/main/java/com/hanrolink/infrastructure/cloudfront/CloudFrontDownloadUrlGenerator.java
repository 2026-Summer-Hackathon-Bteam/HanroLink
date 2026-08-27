package com.hanrolink.infrastructure.cloudfront;

import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;
import software.amazon.awssdk.services.cloudfront.url.SignedUrl;

@Profile("cloudfront")
@Component
public class CloudFrontDownloadUrlGenerator {

  private static final Duration VALID_DURATION = Duration.ofMinutes(30);

  private final CloudFrontUtilities cloudFrontUtilities;

  private final String domainName;

  private final String publicKeyId;

  private final PrivateKey privateKey;

  public CloudFrontDownloadUrlGenerator(
    @Value("${app.storage.cloudfront.domain-name}")
    String domainName,
    @Value("${app.storage.cloudfront.public-key-id}")
    String publicKeyId,
    CloudFrontPrivateKeyProvider privateKeyProvider
  ) {
    this.cloudFrontUtilities = CloudFrontUtilities.create();
    this.domainName = domainName;
    this.publicKeyId = publicKeyId;
    this.privateKey = privateKeyProvider.get();
  }

  public String generate(
    String storageKey
  ) {
    String resourceUrl = "https://" + domainName + "/" + storageKey;

    CannedSignerRequest request =
      CannedSignerRequest
        .builder()
        .resourceUrl(resourceUrl)
        .privateKey(privateKey)
        .keyPairId(publicKeyId)
        .expirationDate(
          Instant.now().plus(VALID_DURATION)
        )
        .build();

    SignedUrl signedUrl = cloudFrontUtilities.getSignedUrlWithCannedPolicy(request);

    return signedUrl.url();
  }
}
