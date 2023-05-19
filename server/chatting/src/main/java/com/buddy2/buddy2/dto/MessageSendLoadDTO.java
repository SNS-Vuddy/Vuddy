package com.buddy2.buddy2.dto;

import com.buddy2.buddy2.entity.Chatroom;
import lombok.Data;

import java.util.List;

@Data
public class MessageSendLoadDTO {
    private String type;
    private List<MessageSendLoadInner> innerList;
}
