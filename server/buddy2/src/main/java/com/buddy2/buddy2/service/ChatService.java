package com.buddy2.buddy2.service;

import com.buddy2.buddy2.config.ChatWebSocketHandler;
import com.buddy2.buddy2.data.KafkaMessageData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import java.net.http.WebSocket;

@Service
public class ChatService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private String receivedMessage = new String();
    ObjectMapper objectMapper = new ObjectMapper();

//    @Autowired
//    private ChatWebSocketHandler chatWebSocketHandler;
//
//    @KafkaListener(topics = "chat-message")
//    public void receiveMessage(String message) {
//        // handle message
//        TextMessage textMessage = new TextMessage(message);
//        receivedMessage = message;
//    }

    public ChatService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(KafkaMessageData message) throws Exception {
        System.out.println("------- 2 --------");
        System.out.println(message);
        System.out.println("------- 2 --------");
        String key = message.getKey();
        String value = objectMapper.writeValueAsString(message.getValue());
        kafkaTemplate.send("chat-message", key, value);
    }
}