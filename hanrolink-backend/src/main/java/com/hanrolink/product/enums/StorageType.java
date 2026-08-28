package com.hanrolink.product.enums;

public enum StorageType {
  AMBIENT("常温"),
  REFRIGERATED("冷蔵"),
  FROZEN("冷凍");

  private final String displayName;

  StorageType(
    String displayName
  ) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
