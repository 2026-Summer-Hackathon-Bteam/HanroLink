package com.hanrolink.account.repository.projection;

import java.util.UUID;

public record ProductImageUploadContextProjection(
  Long businessUserAccountId,
  UUID businessPublicId
) {}
