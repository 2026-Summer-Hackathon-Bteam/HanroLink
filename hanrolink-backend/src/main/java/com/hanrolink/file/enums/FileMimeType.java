package com.hanrolink.file.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileMimeType {
  IMAGE_WEBP("image/webp"),
  APPLICATION_PDF("application/pdf");

  private final String value;

  FileMimeType(
    String value
  ) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
