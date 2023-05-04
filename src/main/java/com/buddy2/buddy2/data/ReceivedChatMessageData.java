package com.buddy2.buddy2.data;

import lombok.Data;

@Data
public class ReceivedChatMessageData {
    private String name;
    private String message;
    private String time;
    private String type;
}
