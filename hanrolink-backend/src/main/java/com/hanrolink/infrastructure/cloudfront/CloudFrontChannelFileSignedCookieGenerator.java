package com.hanrolink.infrastructure.cloudfront;

import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.cookie.CookiesForCustomPolicy;
import software.amazon.awssdk.services.cloudfront.model.CustomSignerRequest;

@Profile("cloudfront")
@Component
public class CloudFrontChannelFileSignedCookieGenerator {

  private static final Duration VALID_DURATION = Duration.ofMinutes(30);

  private final CloudFrontUtilities cloudFrontUtilities;

  private final String domainName;

  private final String publicKeyId;

  private final PrivateKey privateKey;

  public CloudFrontChannelFileSignedCookieGenerator(
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

  public CloudFrontSignedCookieValues generate(
    UUID channelPublicId
  ) {
    String cookiePath = "/channels/"
      + channelPublicId
      + "/files/";
    String resourceUrlPattern = "https://"
      + domainName
      + cookiePath
      + "*";

    CustomSignerRequest request = CustomSignerRequest
      .builder()
      .resourceUrl(resourceUrlPattern)
      .privateKey(privateKey)
      .keyPairId(publicKeyId)
      .expirationDate(
        Instant.now().plus(VALID_DURATION)
      )
      .build();

    CookiesForCustomPolicy cookies =
      cloudFrontUtilities.getCookiesForCustomPolicy(request);

    return new CloudFrontSignedCookieValues(
      cookies.policyHeaderValue(),
      cookies.signatureHeaderValue(),
      cookies.keyPairIdHeaderValue(),
      cookiePath,
      VALID_DURATION
    );
  }
}
