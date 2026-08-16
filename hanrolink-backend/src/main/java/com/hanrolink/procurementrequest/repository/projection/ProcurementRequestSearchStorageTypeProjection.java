package com.hanrolink.procurementrequest.repository.projection;

import com.hanrolink.product.enums.StorageType;

public record ProcurementRequestSearchStorageTypeProjection(
  Long procurementRequestId,
  StorageType storageType
) {}
