package com.buddy2.buddy2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class ChatService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private String receivedMessage = new String();
    ObjectMapper objectMapper = new ObjectMapper();

    public ChatService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String key, String value) throws Exception {
        System.out.println("------- 2 --------");
        System.out.println(key + "   :   " + value);
        System.out.println("------- 2 --------");
        kafkaTemplate.send("chat-message", key, value);
    }
}