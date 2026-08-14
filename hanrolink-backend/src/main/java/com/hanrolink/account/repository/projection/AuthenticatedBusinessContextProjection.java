package com.hanrolink.account.repository.projection;

import com.hanrolink.business.enums.BusinessRole;

public record AuthenticatedBusinessContextProjection(
  Long businessUserAccountId,
  Long businessId,
  BusinessRole businessRole
) {}
