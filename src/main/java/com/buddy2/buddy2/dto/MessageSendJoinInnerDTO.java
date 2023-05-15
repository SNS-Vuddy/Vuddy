package com.buddy2.buddy2.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessageSendJoinInnerDTO {
    private String nickname;
    private Long chatId;
    private List<MessageSendInnerDTO> messageList;
}
