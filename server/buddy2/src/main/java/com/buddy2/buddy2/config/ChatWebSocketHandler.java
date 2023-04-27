package com.buddy2.buddy2.config;

import com.buddy2.buddy2.data.ChatMessageData;
import com.buddy2.buddy2.data.KafkaMessageData;
import com.buddy2.buddy2.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
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
        System.out.println(session.getId());  // 44a4e7cb-72a9-efce-f41f-a7ba84955b13
        System.out.println(session.toString());  // StandardWebSocketSession[id=44a4e7cb-72a9-efce-f41f-a7ba84955b13, uri=ws://localhost:8080/chat]
        System.out.println(session.getUri());  // ws://localhost:8080/chat
        System.out.println(session.getPrincipal());  // null
        System.out.println(session.getAcceptedProtocol());  //
        System.out.println(session.getHandshakeHeaders());  // [sec-websocket-version:"13", sec-websocket-key:"h745gLfbf8MSaMMQy9RGYw==", connection:"Upgrade", upgrade:"websocket", sec-websocket-extensions:"permessage-deflate; client_max_window_bits", host:"localhost:8080"]
        System.out.println("===================접속=====================");
//        sessions.put(session.getId(), session);
        sessionsList.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(session.getId());
        sessionsList.remove(session);
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