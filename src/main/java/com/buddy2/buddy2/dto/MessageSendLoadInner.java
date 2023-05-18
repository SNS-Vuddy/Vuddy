package com.buddy2.buddy2.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageSendLoadInner {
    private Long chatId;
    private String lastChat;
    private String nickname;
    private String time;
    private String profileImg;
}
