package com.hanrolink.onboarding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OnboardingExceptionHandler {

  @ExceptionHandler(OnboardingAlreadyExistsException.class)
  public ProblemDetail alreadyExists(
    OnboardingAlreadyExistsException exception
  ) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
      HttpStatus.CONFLICT,
      "現在の状態では初期登録を実行できません。"
    );

    problem.setProperty(
        "code",
        "ONBOARDING_ALREADY_EXISTS"
    );

    return problem;
  }
}
