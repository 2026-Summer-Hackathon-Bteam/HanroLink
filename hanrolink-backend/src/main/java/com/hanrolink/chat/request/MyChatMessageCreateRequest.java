package com.hanrolink.chat.request;

import java.util.List;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.AssertTrue;

public record MyChatMessageCreateRequest(
  String body,

  List<MultipartFile> file,

  String displayFilename
) {

  @AssertTrue(message = "本文またはファイルのどちらかは必須です")
  public boolean isContentPresent() {
    return StringUtils.hasText(body) || (file != null && !file.isEmpty());
  }
}
