package com.buddy2.buddy2.data;

import lombok.Data;


@Data
public class ChatMessageData {
    private String nickname;
    private String message;
    private String time;
    private String type;
    private String chatroomTitle;
    private Long chatId;
}
