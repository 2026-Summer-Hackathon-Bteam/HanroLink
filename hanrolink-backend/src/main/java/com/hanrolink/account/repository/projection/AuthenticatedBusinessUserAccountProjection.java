package com.hanrolink.account.repository.projection;

import com.hanrolink.business.enums.BusinessRole;

public record AuthenticatedBusinessUserAccountProjection(
  Long id,
  BusinessRole role
) {}
