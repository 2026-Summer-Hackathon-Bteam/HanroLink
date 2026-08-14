package com.hanrolink.account.repository.projection;

import com.hanrolink.account.enums.BusinessUserAccountRole;

public record AuthenticatedBusinessUserAccountProjection(
  Long id,
  BusinessUserAccountRole role
) {}
