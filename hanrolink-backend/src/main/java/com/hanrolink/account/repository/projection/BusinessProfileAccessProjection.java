package com.hanrolink.account.repository.projection;

import java.util.UUID;

import com.hanrolink.business.enums.BusinessRole;

public record BusinessProfileAccessProjection(
  UUID businessPublicId,
  BusinessRole businessRole
) {}
