package com.hanrolink.infrastructure.s3;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Profile("s3")
@Component
public class S3UploadUrlGenerator {

  private static final Duration VALID_DURATION = Duration.ofMinutes(5);

  private final S3Presigner s3Presigner;

  private final String bucketName;

  public S3UploadUrlGenerator(
    S3Presigner s3Presigner,
    @Value("${app.storage.s3.bucket-name}")
    String bucketName
  ) {
    this.s3Presigner = s3Presigner;
    this.bucketName = bucketName;
  }

  public String generate(
    String storageKey,
    String mimeType
  ) {
    PutObjectRequest putObjectRequest =
      PutObjectRequest
        .builder()
        .bucket(bucketName)
        .key(storageKey)
        .contentType(mimeType)
        .build();

    PutObjectPresignRequest presignRequest =
      PutObjectPresignRequest
        .builder()
        .signatureDuration(VALID_DURATION)
        .putObjectRequest(putObjectRequest)
        .build();

    PresignedPutObjectRequest presignedRequest =
      s3Presigner.presignPutObject(presignRequest);

    return presignedRequest
      .url()
      .toExternalForm();
  }
}
