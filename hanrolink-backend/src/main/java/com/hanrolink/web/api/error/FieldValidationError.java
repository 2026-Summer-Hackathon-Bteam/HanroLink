package com.hanrolink.web.api.error;

import java.util.Map;

public record FieldValidationError(
  String field,
  String code,
  Map<String, Object> parameters
) {}
