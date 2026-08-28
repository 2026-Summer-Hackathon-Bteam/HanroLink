package com.hanrolink.procurementrequest.repository.projection;

import java.util.UUID;

public record ProcurementRequestSearchResultProjection(
  Long id,
  UUID publicId,
  String title,
  String description,
  String productCategoryName,
  UUID businessPublicId,
  String businessName
) {}
