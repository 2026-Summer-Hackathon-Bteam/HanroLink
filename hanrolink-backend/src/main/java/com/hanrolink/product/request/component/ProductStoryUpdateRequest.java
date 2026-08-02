package com.hanrolink.product.request.component;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductStoryUpdateRequest(
  @NotNull
  @Positive
  Long id,

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

  MultipartFile imageFile
) {}
