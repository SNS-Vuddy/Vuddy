package com.buddy2.buddy2.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessageSendInnerDTO {
    private String nickname;
    private String message;
    private String time;
    private Long chatId;
    private List<MessageSendInnerDTO> messageList;
}
