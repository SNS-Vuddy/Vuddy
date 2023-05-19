package com.buddy2.buddy2.dto;

import lombok.Data;

@Data
public class MessageSendDTO {
    private String type;
    private MessageSendInnerDTO data;
}
