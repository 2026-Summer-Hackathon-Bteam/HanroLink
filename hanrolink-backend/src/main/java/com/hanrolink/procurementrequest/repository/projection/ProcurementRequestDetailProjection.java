package com.hanrolink.procurementrequest.repository.projection;

import java.util.UUID;

public record ProcurementRequestDetailProjection(
  Long id,
  UUID publicId,
  Long buyerBusinessId,
  UUID buyerBusinessPublicId,
  String buyerBusinessName,
  Short productCategoryId,
  String productCategoryName,
  String title,
  String description,
  String requiredTradeTerms,
  Integer desiredUnitPrice,
  Short deliveryShelfLifeDays
) {}
