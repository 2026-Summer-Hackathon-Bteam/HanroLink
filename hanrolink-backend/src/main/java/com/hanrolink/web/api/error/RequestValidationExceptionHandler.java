package com.hanrolink.web.api.error;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;

@RestControllerAdvice
public class RequestValidationExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Set<String> EXPOSED_PARAMETERS =
    Set.of("min", "max", "value");

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException exception,
    HttpHeaders headers,
    HttpStatusCode status,
    WebRequest request
  ) {
    List<FieldValidationError> errors =
      exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(this::toValidationError)
        .toList();

    ProblemDetail problem =
      ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST,
        "リクエストの入力値が不正です"
      );

    problem.setType(URI.create("urn:hanrolink:problem:validation-error"));
    problem.setTitle("Validation Error");
    problem.setProperty("errors", errors);

    return handleExceptionInternal(
      exception,
      problem,
      headers,
      status,
      request
    );
  }

  private FieldValidationError toValidationError(
    FieldError fieldError
  ) {
    return new FieldValidationError(
      fieldError.getField(),
      toApiCode(fieldError.getCode()),
      extractParameters(fieldError)
    );
  }

  private String toApiCode(String validationCode) {
    return switch (validationCode) {
      case "NotBlank", "NotEmpty", "NotNull" ->
        "REQUIRED";
      case "Size" ->
        "INVALID_SIZE";
      case "Positive" ->
        "POSITIVE_VALUE_REQUIRED";
      case "Min" ->
        "MIN_VALUE";
      case "Max" ->
        "MAX_VALUE";
      case "FutureOrPresent" ->
        "FUTURE_OR_PRESENT_REQUIRED";
      case null, default ->
        "INVALID_VALUE";
    };
  }

  private Map<String, Object> extractParameters(
    FieldError fieldError
  ) {
    if (!fieldError.contains(ConstraintViolation.class)) {
      return Map.of();
    }

    ConstraintViolation<?> violation = fieldError.unwrap(ConstraintViolation.class);

    Map<String, Object> parameters = new LinkedHashMap<>();

    violation
      .getConstraintDescriptor()
      .getAttributes()
      .forEach((name, value) -> {
        if (EXPOSED_PARAMETERS.contains(name)) {
          parameters.put(name, value);
        }
      });

    return Map.copyOf(parameters);
  }
}
