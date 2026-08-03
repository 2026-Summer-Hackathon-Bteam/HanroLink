package com.hanrolink.product.request.component;

import org.springframework.web.multipart.MultipartFile;

import com.hanrolink.web.validation.NotEmptyFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductStoryCreateRequest(
  @NotNull
  @Min(1)
  @Max(4)
  Short position,

  @NotNull
  @Positive
  Short productStorySectionTemplateId,

  @NotBlank
  @Size(max = 255)
  String body,

  @NotNull
  @NotEmptyFile
  MultipartFile imageFile
) {}
