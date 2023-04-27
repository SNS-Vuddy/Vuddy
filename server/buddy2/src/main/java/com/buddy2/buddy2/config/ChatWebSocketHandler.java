package com.buddy2.buddy2.config;

import com.buddy2.buddy2.data.ChatMessageData;
import com.buddy2.buddy2.data.KafkaMessageData;
import com.buddy2.buddy2.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;

    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private List<WebSocketSession> sessionsList = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("===================접속=====================");
        System.out.println(session.getId());
        System.out.println("===================접속=====================");
        sessions.put(session.getId(), session);
        sessionsList.add(session);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("===================수신=====================");
        System.out.println(session.getId());
        System.out.println("수신 메시지 : " + message.getPayload());
        System.out.println("===================수신=====================");



        ChatMessageData clientMessageData = objectMapper.readValue(message.getPayload(), ChatMessageData.class);

        ChatMessageData chatMessage = new ChatMessageData();
        KafkaMessageData kafkaMessage = new KafkaMessageData();

        chatMessage.setName(clientMessageData.getName());
        chatMessage.setMessage(clientMessageData.getMessage());
        // 현지 시각을 불러와서 형식에 맞게 집어넣는다.
        LocalDateTime timeNow = LocalDateTime.now(ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        chatMessage.setTime(timeNow.format(dateTimeFormatter));

        kafkaMessage.setKey(clientMessageData.getName());
        kafkaMessage.setValue(chatMessage);
        chatService.sendMessage(kafkaMessage);
    }

    @KafkaListener(topics = "chat-message")
    protected void receiveTextMessage(String message) throws Exception {
        System.out.println("------- 3 --------");
        System.out.println(message);
        System.out.println("------- 3 --------");
        for (WebSocketSession session : sessionsList){
            System.out.println(session.getId());
            session.sendMessage(new TextMessage(message));
        }
    }

//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        // handle payload
//        System.out.println(payload);
//        session.sendMessage(new TextMessage("Hello Client!"));
//    }

}