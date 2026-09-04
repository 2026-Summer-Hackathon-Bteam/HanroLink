package com.hanrolink.web.error;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RequestValidationErrorHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException exception,
    HttpHeaders headers,
    HttpStatusCode status,
    WebRequest request
  ) {
    Map<String, List<String>> errors =
      exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .collect(
          Collectors.groupingBy(
            fieldError -> fieldError.getField(),
            LinkedHashMap::new,
            Collectors.mapping(
              fieldError ->
                Objects.requireNonNullElse(
                  fieldError.getDefaultMessage(),
                  "入力内容を確認してください"
                ),
              Collectors.toList()
            )
          )
        );

    ProblemDetail problem =
      ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST,
        "入力内容を確認してください"
      );

    problem.setType(
      URI.create("urn:hanrolink:problem:validation-error")
    );
    problem.setTitle("Validation Error");
    problem.setProperty("errors", errors);

    return handleExceptionInternal(exception, problem, headers, status, request);
  }
}
