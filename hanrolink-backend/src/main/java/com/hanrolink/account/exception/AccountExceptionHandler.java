package com.hanrolink.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountExceptionHandler {

  @ExceptionHandler(
    UnsupportedJwtAccountRoleException.class
  )
  public ProblemDetail unsupportedAccountRole() {
    var problem = ProblemDetail.forStatusAndDetail(
      HttpStatus.CONFLICT,
      "この操作を実行する権限がありません。"
    );

    problem.setProperty("code", "ACCOUNT_ROLE_NOT_ALLOWED");

    return problem;
  }
}
