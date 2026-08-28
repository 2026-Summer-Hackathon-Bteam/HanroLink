package com.hanrolink.infrastructure.cloudfront;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("cloudfront")
@Component
public class CloudFrontPrivateKeyProvider {

  private final PrivateKey privateKey;

  public CloudFrontPrivateKeyProvider(
    @Value("${app.storage.cloudfront.private-key}")
    String privateKeyPem,
    @Value("${app.storage.cloudfront.private-key-path}")
    String privateKeyPath
  ) {
    this.privateKey = loadPrivateKey(
      privateKeyPem,
      privateKeyPath
    );
  }

  public PrivateKey get() {
    return privateKey;
  }

  private PrivateKey loadPrivateKey(
    String privateKeyPem,
    String privateKeyPath
  ) {
    boolean hasPrivateKey =
      privateKeyPem != null
        && !privateKeyPem.isBlank();

    boolean hasPrivateKeyPath =
      privateKeyPath != null
        && !privateKeyPath.isBlank();

    if (!hasPrivateKey && !hasPrivateKeyPath) {
      throw new IllegalStateException(
        "CloudFront署名用秘密鍵が設定されていません"
      );
    }

    if (hasPrivateKey && hasPrivateKeyPath) {
      throw new IllegalStateException(
        "CloudFront署名用秘密鍵の設定が重複しています"
      );
    }

    if (hasPrivateKey) {
      return parsePrivateKey(privateKeyPem);
    }

    return loadPrivateKeyFromFile(
      Path.of(privateKeyPath)
    );
  }

  private PrivateKey loadPrivateKeyFromFile(
    Path privateKeyPath
  ) {
    try {
      String pem = Files.readString(
        privateKeyPath,
        StandardCharsets.US_ASCII
      );

      return parsePrivateKey(pem);
    } catch (
      IOException exception
    ) {
      throw new IllegalStateException(
        "CloudFront署名用秘密鍵の読み込みに失敗しました",
        exception
      );
    }
  }

  private PrivateKey parsePrivateKey(
    String pem
  ) {
    try {
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
      GeneralSecurityException
        | IllegalArgumentException exception
    ) {
      throw new IllegalStateException(
        "CloudFront署名用秘密鍵の解析に失敗しました",
        exception
      );
    }
  }
}
