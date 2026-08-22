package com.hanrolink.negotiationrequest.snapshot.component;

import com.hanrolink.product.enums.StorageType;

public record StorageTypeSnapshot(
  StorageType value,
  String displayName
) {}
