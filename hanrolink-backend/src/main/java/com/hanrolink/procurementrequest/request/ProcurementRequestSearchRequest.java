package com.hanrolink.procurementrequest.request;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.hanrolink.product.enums.StorageType;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProcurementRequestSearchRequest(
  List<@Positive Short> productCategoryGroupIds,

  List<@Positive Short> productCategoryIds,

  Set<StorageType> storageTypes,

  @Size(max = 255)
  String keyword,

  @Parameter(
    schema = @Schema(
      type = "integer",
      format = "int32",
      defaultValue = "1"
    )
  )
  @Min(1)
  Integer page,

  @Parameter(
    schema = @Schema(
      type = "integer",
      format = "int32",
      defaultValue = "20"
    )
  )
  @Min(1)
  @Max(100)
  Integer pageSize
) {

  public ProcurementRequestSearchRequest {
    page = Objects.requireNonNullElse(page, 1);
    pageSize = Objects.requireNonNullElse(pageSize, 20);
  }
}
