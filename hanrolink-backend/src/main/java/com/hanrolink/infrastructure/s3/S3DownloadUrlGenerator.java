package com.hanrolink.infrastructure.s3;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Profile("s3")
@Component
public class S3DownloadUrlGenerator {

  private static final Duration VALID_DURATION = Duration.ofMinutes(30);

  private final S3Presigner s3Presigner;

  private final String bucketName;

  public S3DownloadUrlGenerator(
    S3Presigner s3Presigner,
    @Value("${app.storage.s3.bucket-name}")
    String bucketName
  ) {
    this.s3Presigner = s3Presigner;
    this.bucketName = bucketName;
  }

  public String generate(
    String storageKey
  ) {
    GetObjectRequest getObjectRequest =
      GetObjectRequest
        .builder()
        .bucket(bucketName)
        .key(storageKey)
        .build();

    GetObjectPresignRequest presignRequest =
      GetObjectPresignRequest
        .builder()
        .signatureDuration(VALID_DURATION)
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedRequest =
      s3Presigner.presignGetObject(presignRequest);

    return presignedRequest
      .url()
      .toExternalForm();
  }
}
