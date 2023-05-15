package com.buddy2.buddy2.dto;

import lombok.Data;

@Data
public class MessageSendJoin {
    private String type;
    private MessageSendJoinInnerDTO data;
}
