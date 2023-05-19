package com.buddy2.buddy2.data;

import lombok.Data;

@Data
public class KafkaMessageData {
    private String key;
    private ChatMessageData value;
}
