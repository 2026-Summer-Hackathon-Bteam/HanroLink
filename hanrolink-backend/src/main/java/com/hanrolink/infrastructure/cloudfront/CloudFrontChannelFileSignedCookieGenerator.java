package com.hanrolink.infrastructure.cloudfront;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
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
    @Value("${app.storage.cloudfront.private-key-path}")
    String privateKeyPath
  ) {
    this.cloudFrontUtilities = CloudFrontUtilities.create();
    this.domainName = domainName;
    this.publicKeyId = publicKeyId;
    this.privateKey = loadPrivateKey(Path.of(privateKeyPath));
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

  private PrivateKey loadPrivateKey(
    Path privateKeyPath
  ) {
    try {
      String pem = Files.readString(
        privateKeyPath,
        StandardCharsets.US_ASCII
      );

      String encodedKey = pem
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s", "");

      byte[] privateKeyBytes = Base64
        .getDecoder()
        .decode(encodedKey);

      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

      return KeyFactory
        .getInstance("RSA")
        .generatePrivate(keySpec);
    } catch (
      IOException
        | GeneralSecurityException
        | IllegalArgumentException exception
    ) {
      throw new IllegalStateException(
        "CloudFront署名用秘密鍵の読み込みに失敗しました",
        exception
      );
    }
  }
}
