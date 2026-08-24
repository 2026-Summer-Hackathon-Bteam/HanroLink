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
    @Value("${app.storage.cloudfront.private-key-path}")
    String privateKeyPath
  ) {
    this.cloudFrontUtilities = CloudFrontUtilities.create();
    this.domainName = domainName;
    this.publicKeyId = publicKeyId;
    this.privateKey = loadPrivateKey(Path.of(privateKeyPath));
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
