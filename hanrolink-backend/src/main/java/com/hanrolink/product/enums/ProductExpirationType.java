package com.hanrolink.product.enums;

public enum ProductExpirationType {
  BEST_BEFORE("賞味期限"),
  USE_BY("消費期限"),
  NOT_APPLICABLE("期限表示対象外");

  private final String displayName;

  ProductExpirationType(
    String displayName
  ) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
