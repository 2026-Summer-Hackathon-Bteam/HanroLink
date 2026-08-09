package com.hanrolink.security.authorization.policy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("""
  (
    hasRole('SUPPLIER')
    and hasAuthority('REVIEW_APPROVED')
  )
  or (
    hasRole('BUYER')
    and hasAuthority('REVIEW_APPROVED')
  )
  """)
public @interface RequiresApprovedBusinessUserAccount {}
