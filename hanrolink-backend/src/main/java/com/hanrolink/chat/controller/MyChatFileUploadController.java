package com.hanrolink.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.chat.api.ChatApi;
import com.hanrolink.chat.request.MyChatFileUploadCreateRequest;
import com.hanrolink.chat.response.MyChatFileUploadCreateResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class MyChatFileUploadController {

  // サプライヤー、バイヤー利用可能
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(ChatApi.V1.FILE_UPLOADS)
  public ResponseEntity<MyChatFileUploadCreateResponse> create(
    @Valid @RequestBody MyChatFileUploadCreateRequest request
  ) {

    // TODO: アップロード情報を登録後、201 Createdで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
