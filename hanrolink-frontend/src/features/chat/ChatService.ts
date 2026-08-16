import type { ChatDetail, ChatMessages } from "./ChatTypes";
import { chatDetailMock, chatMessagesMock } from "./ChatMock";

export function getChatMessages(channelId: string):Promise<ChatMessages> {
    void channelId  // APIのパスに使う
    
    return Promise.resolve(chatMessagesMock)
}

export function getChatDetail(channelId: string): Promise<ChatDetail> {
    void channelId  // APIのパスに使う

    return Promise.resolve(chatDetailMock)
}