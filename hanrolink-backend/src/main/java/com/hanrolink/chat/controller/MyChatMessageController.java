package com.hanrolink.chat.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.chat.api.ChatApi;
import com.hanrolink.chat.request.MyChatMessageCreateRequest;
import com.hanrolink.chat.response.MyChatMessageListResponse;
import com.hanrolink.product.request.SupplierProductCreateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class MyChatMessageController {

  // 当事者のバイヤー、サプライヤーのみ利用可能
  @Operation(
    requestBody =
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
          mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
          schema = @Schema(
            implementation = SupplierProductCreateRequest.class
          )
        )
      )
  )
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(ChatApi.V1.MESSAGES)
  public ResponseEntity<Void> create(
    @PathVariable UUID channelId,
    @Parameter(hidden = true)
    @Valid
    @ModelAttribute MyChatMessageCreateRequest request
  ) {

    // TODO: メッセージを登録後、201 Createdで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  // 当事者のバイヤー、サプライヤーのみ利用可能
  @GetMapping(ChatApi.V1.MESSAGES)
  public ResponseEntity<List<MyChatMessageListResponse>> list(
    @PathVariable UUID channelId
  ) {

    // TODO: チャンネルに紐づくメッセージ情報一覧を取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
