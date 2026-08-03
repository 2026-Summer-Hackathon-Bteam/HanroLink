package com.hanrolink.onboarding.response;

import com.hanrolink.account.enums.BusinessUserAccountRegistrationApiStatus;

public record OnboardingCreateResponse(
  BusinessUserAccountRegistrationApiStatus businessUserAccountRegistrationStatus
) {}
