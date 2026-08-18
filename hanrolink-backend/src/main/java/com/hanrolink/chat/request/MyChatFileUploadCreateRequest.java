package com.hanrolink.chat.request;

import com.hanrolink.file.enums.FileMimeType;
import com.hanrolink.file.policy.ImageFilePolicy;
import com.hanrolink.file.policy.PdfFilePolicy;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MyChatFileUploadCreateRequest(
  @NotNull
  FileMimeType mimeType,

  @NotBlank
  @Size(max = 255)
  String displayFilename,

  @NotNull
  @Positive
  @Max(
    value = PdfFilePolicy.MAX_FILE_SIZE_BYTES,
    message = "ファイルの容量が上限を超えています"
  )
  Long fileSizeBytes
) {
  @AssertTrue(message = "ファイルの容量が上限を超えています")
  public boolean isFileSizeWithinLimit() {
    if (mimeType == null || fileSizeBytes == null) {
      return true;
    }

    return switch (mimeType) {
      case IMAGE_WEBP ->
        fileSizeBytes <= ImageFilePolicy.MAX_FILE_SIZE_BYTES;
      case APPLICATION_PDF ->
        fileSizeBytes <= PdfFilePolicy.MAX_FILE_SIZE_BYTES;
    };
  }
}
