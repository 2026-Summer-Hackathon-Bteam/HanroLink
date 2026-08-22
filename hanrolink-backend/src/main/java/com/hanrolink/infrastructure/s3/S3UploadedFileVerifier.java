package com.hanrolink.infrastructure.s3;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.hanrolink.file.enums.FileMimeType;
import com.hanrolink.file.policy.ImageFilePolicy;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Profile("s3")
@Component
public class S3UploadedFileVerifier {

  private final S3Client s3Client;

  private final String bucketName;

  public S3UploadedFileVerifier(
    S3Client s3Client,
    @Value("${app.storage.s3.bucket-name}")
    String bucketName
  ) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;
  }

  public boolean isValidWebp(
    String storageKey,
    Long expectedFileSizeBytes
  ) {
    try {
      HeadObjectResponse metadata = getMetadata(storageKey);
      if (!Objects.equals(metadata.contentType(), FileMimeType.IMAGE_WEBP.getValue())) {
        return false;
      }
      if (!Objects.equals(metadata.contentLength(), expectedFileSizeBytes)) {
        return false;
      }

      byte[] imageBytes = getFileBytes(storageKey);
      if (!hasWebpSignature(imageBytes)) {
        return false;
      }

      return canDecodeWebp(imageBytes);
    } catch (S3Exception exception) {
      if (exception.statusCode() == 404) {
        return false;
      }

      throw exception;
    }
  }

  public boolean isValidPdf(
    String storageKey,
    Long expectedFileSizeBytes
  ) {
    try {
      HeadObjectResponse metadata = getMetadata(storageKey);
      if (!Objects.equals(metadata.contentType(), FileMimeType.APPLICATION_PDF.getValue())) {
        return false;
      }
      if (!Objects.equals(metadata.contentLength(), expectedFileSizeBytes)) {
        return false;
      }

      byte[] fileBytes = getFileBytes(storageKey);
      if (!hasPdfSignature(fileBytes)) {
        return false;
      }

      return canParsePdf(fileBytes);
    } catch (S3Exception exception) {
      if (exception.statusCode() == 404) {
        return false;
      }

      throw exception;
    }
  }

  private HeadObjectResponse getMetadata(
    String storageKey
  ) {
    HeadObjectRequest request =
      HeadObjectRequest
        .builder()
        .bucket(bucketName)
        .key(storageKey)
        .build();

    return s3Client.headObject(request);
  }

  private byte[] getFileBytes(
    String storageKey
  ) {
    GetObjectRequest request =
      GetObjectRequest
        .builder()
        .bucket(bucketName)
        .key(storageKey)
        .build();

    ResponseBytes<GetObjectResponse> response =
      s3Client.getObjectAsBytes(request);

    return response.asByteArray();
  }

  private boolean hasWebpSignature(
    byte[] imageBytes
  ) {
    return imageBytes.length >= 12
      && imageBytes[0] == 'R'
      && imageBytes[1] == 'I'
      && imageBytes[2] == 'F'
      && imageBytes[3] == 'F'
      && imageBytes[8] == 'W'
      && imageBytes[9] == 'E'
      && imageBytes[10] == 'B'
      && imageBytes[11] == 'P';
  }

  private boolean hasPdfSignature(
    byte[] fileBytes
  ) {
    return fileBytes.length >= 5
      && fileBytes[0] == '%'
      && fileBytes[1] == 'P'
      && fileBytes[2] == 'D'
      && fileBytes[3] == 'F'
      && fileBytes[4] == '-';
  }

  private boolean canDecodeWebp(
    byte[] imageBytes
  ) {
    try (
      ByteArrayInputStream byteStream =
        new ByteArrayInputStream(imageBytes);

      ImageInputStream imageStream =
        ImageIO.createImageInputStream(byteStream)
    ) {
      if (imageStream == null) {
        return false;
      }

      Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
      if (!readers.hasNext()) {
        return false;
      }

      ImageReader reader = readers.next();

      try {
        if (!reader.getFormatName().equalsIgnoreCase("webp")) {
          return false;
        }

        reader.setInput(imageStream, true, true);

        int width = reader.getWidth(0);
        int height = reader.getHeight(0);

        if ((long) width * height > ImageFilePolicy.MAX_DECODED_PIXEL_COUNT) {
          return false;
        }

        BufferedImage image = reader.read(0);

        return image != null;
      } finally {
        reader.dispose();
      }
    } catch (IOException | RuntimeException exception) {
      return false;
    }
  }

  private boolean canParsePdf(
    byte[] fileBytes
  ) {
    try (
      PDDocument document = Loader.loadPDF(fileBytes)
    ) {
      if (document.isEncrypted()) {
        return false;
      }

      return document.getNumberOfPages() > 0;
    } catch (IOException | RuntimeException exception) {
      return false;
    }
  }
}
