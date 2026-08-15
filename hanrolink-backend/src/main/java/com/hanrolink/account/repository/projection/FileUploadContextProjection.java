package com.hanrolink.account.repository.projection;

import java.util.UUID;

public record FileUploadContextProjection(
  Long businessUserAccountId,
  UUID businessPublicId
) {}
