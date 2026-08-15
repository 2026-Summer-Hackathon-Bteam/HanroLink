package com.hanrolink.security.authentication;

public record AuthenticatedUser(
  String sub,
  String email
) {}
