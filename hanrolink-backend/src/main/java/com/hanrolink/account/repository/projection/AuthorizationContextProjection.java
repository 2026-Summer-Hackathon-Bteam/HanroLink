package com.hanrolink.account.repository.projection;

import com.hanrolink.business.enums.BusinessReviewStatus;
import com.hanrolink.business.enums.BusinessRole;

public record AuthorizationContextProjection(
  BusinessRole businessRole,
  BusinessReviewStatus businessReviewStatus
) {}
