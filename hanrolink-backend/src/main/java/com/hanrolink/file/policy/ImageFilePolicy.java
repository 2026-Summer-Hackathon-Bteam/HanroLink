package com.hanrolink.file.policy;

public final class ImageFilePolicy {

  public static final long MAX_FILE_SIZE_BYTES = 300L * 1024L;
  public static final long MAX_DECODED_PIXEL_COUNT = 4_000_000L;

  private ImageFilePolicy() {}
}
