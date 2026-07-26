package com.hanrolink.web.validation;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyFileValidator
  implements ConstraintValidator<NotEmptyFile, MultipartFile> {

  @Override
  public boolean isValid(
    MultipartFile file,
    ConstraintValidatorContext context
  ) {
    return file != null && !file.isEmpty();
  }
}
