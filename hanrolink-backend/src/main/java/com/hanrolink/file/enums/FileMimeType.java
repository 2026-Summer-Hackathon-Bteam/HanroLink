package com.hanrolink.file.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileMimeType {
  IMAGE_WEBP("image/webp", "webp"),
  APPLICATION_PDF("application/pdf", "pdf");

  private final String value;
  private final String extension;

  FileMimeType(
    String value,
    String extension
  ) {
    this.value = value;
    this.extension = extension;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public String getExtension() {
    return extension;
  }
}
