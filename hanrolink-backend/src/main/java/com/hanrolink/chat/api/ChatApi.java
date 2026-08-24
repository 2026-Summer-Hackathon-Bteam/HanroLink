package com.hanrolink.chat.api;

import com.hanrolink.web.api.ApiPath;

public final class ChatApi {

  public static final class V1 {
  
    private static final String BASE = ApiPath.API_V1 + "/chats";
    public static final String MINE = ApiPath.API_V1 + "/me/chats";
    public static final String BY_ID = BASE + "/{channelId}";
    public static final String MESSAGES = BY_ID + "/messages";
    public static final String FILE_UPLOADS = BY_ID + "/file-uploads";
    public static final String FILE_ACCESS = BY_ID + "/file-access";

    private V1() {}
  }

  private ChatApi() {}
}
