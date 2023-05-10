package com.buddy2.buddy2.config;

import com.buddy2.buddy2.data.ChatMessageData;
import com.buddy2.buddy2.domain.CurrentChatrooms;
import com.buddy2.buddy2.dto.RedisMessageDTO;
import com.buddy2.buddy2.entity.Chatroom;
import com.buddy2.buddy2.entity.Message;
import com.buddy2.buddy2.repository.ChatroomRepository;
import com.buddy2.buddy2.repository.MessageRepository;
import com.buddy2.buddy2.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

//    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
//    private List<WebSocketSession> sessionsList = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTemplate<String, String> redisMessageTemplate;


    public void addValueToRedis (String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }


    Map<Long, CurrentChatrooms> currentChatroomsMap = new ConcurrentHashMap<>();
//    Map<String, Integer> chatNumberMap = new ConcurrentHashMap<>();

//    Long chatNum = chatroomRepository.findWithChatId().getChatId();
//    Long chatNumber = chatNum != null? chatNum : 0L;
//    Long chatNumber = 0L;

//    List<CurrentChatrooms> currentChatroomsList = new ArrayList<>();

    Long nowLocation = 0L;

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
//        sessionsList.add(session);
//        String nickname = "";
//        if (session.getUri() != null) {
//            nickname = session.getUri().getQuery();
//        }
//        String data = session.getUri().getQuery();
//        String json = URLDecoder.decode(data, "UTF-8");
//        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
//        String username = obj.get("username").getAsString();
//        String password = obj.get("password").getAsString();
//        System.out.println(nickname);

        // 보내주는 정보 : 채팅방 아이디

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(session.getId());
//        sessionsList.remove(session);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("===================수신=====================");
        System.out.println(session.getId());
        System.out.println("수신 메시지 : " + message.getPayload());
        System.out.println("===================수신=====================");



        ChatMessageData clientMessageData = objectMapper.readValue(message.getPayload(), ChatMessageData.class);

        ChatMessageData chatMessage = new ChatMessageData();



        String messageType = clientMessageData.getType();
        System.out.println(messageType);
        System.out.println(clientMessageData.getType());
        System.out.println(messageType == "OPEN");
        String roomTitle = clientMessageData.getChatroomTitle();
        if (messageType.equals("OPEN")) {
            Long chatNumber = 0L;
            Chatroom lastChatroom = chatroomRepository.findFirstByOrderByChatIdDesc();
            if (lastChatroom != null) {
                chatNumber = lastChatroom.getChatId();
            }
            chatNumber++;
            CurrentChatrooms currentChatrooms = CurrentChatrooms.builder()
                    .chatroomTitle(clientMessageData.getChatroomTitle())
                    .chatId(chatNumber)
                    .nickname(clientMessageData.getNickname())
                    .webSocketSession(session)
                    .build();
            currentChatroomsMap.put(chatNumber,currentChatrooms);
            chatMessage.setChatId(chatNumber);
            System.out.println(chatMessage.getChatId());
            chatMessage.setMessage(clientMessageData.getMessage());
            chatMessage.setNickname(clientMessageData.getNickname());
            nowLocation = chatNumber;

            LocalDateTime timeNow = LocalDateTime.now(ZoneId.systemDefault());
            chatMessage.setTime(formatDateTime(timeNow));
            chatMessage.setChatroomTitle(clientMessageData.getChatroomTitle());
            chatMessage.setType(clientMessageData.getType());

            Chatroom chatroom = Chatroom.builder()
                    .lastChat(chatMessage.getMessage())
                    .nickname(chatMessage.getNickname())
                    .time(chatMessage.getTime())
                    .title(chatMessage.getChatroomTitle())
                    .build();
            chatroomRepository.save(chatroom);
        }
        else if (messageType.equals("JOIN")) {
            CurrentChatrooms currentChatrooms = currentChatroomsMap.get(clientMessageData.getChatId());
            if (currentChatrooms == null) {
                for (Chatroom room : chatroomRepository.findByChatId(clientMessageData.getChatId())) {
                    if (Objects.equals(room.getChatId(), clientMessageData.getChatId())) {
                        currentChatrooms = CurrentChatrooms.builder()
                                .chatId(room.getChatId())
                                .chatroomTitle(room.getTitle())
                                .nickname(clientMessageData.getNickname())
                                .webSocketSession(session)
                                .build();
                        currentChatroomsMap.put(room.getChatId(), currentChatrooms);
                        nowLocation = room.getChatId();
                        break;
                    }
                }
            } else {
                currentChatrooms.addChatMember(clientMessageData.getNickname(), session);
                currentChatroomsMap.replace(clientMessageData.getChatId(), currentChatrooms);
                nowLocation = clientMessageData.getChatId();
            }
            if (currentChatrooms == null) {
//                log.debug("없는 방에 JOIN 요청");
                return;
            }

            chatMessage.setChatId(clientMessageData.getChatId());
            chatMessage.setMessage(clientMessageData.getMessage());
            chatMessage.setNickname(clientMessageData.getNickname());


            LocalDateTime timeNow = LocalDateTime.now(ZoneId.systemDefault());
            chatMessage.setTime(formatDateTime(timeNow));
            RedisMessageDTO messageNow = RedisMessageDTO.builder()
                    .chatId(chatMessage.getChatId())
                    .content(chatMessage.getMessage())
                    .time(chatMessage.getTime())
                    .nickname(chatMessage.getNickname())
                    .build();
            redisMessageTemplate.opsForList().rightPush("chatroomId-" + chatMessage.getChatId().toString(), objectMapper.writeValueAsString(messageNow));
        }
        else if (messageType.equals("LOAD")) {
            System.out.println(clientMessageData.getNickname());
            List<Chatroom> chatroomList = chatroomRepository.findWithNickname(clientMessageData.getNickname());
            chatMessage.setChatId(clientMessageData.getChatId());
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatroomList)));
        }
        else if (messageType.equals("EXIT")) {
            CurrentChatrooms currentChatrooms = currentChatroomsMap.get(nowLocation);
            currentChatrooms.removeChatMember(clientMessageData.getNickname());
            nowLocation = 0L;
        }
        else if (messageType.equals("LEAVE")) {
            CurrentChatrooms currentChatrooms = currentChatroomsMap.get(nowLocation);
            currentChatrooms.removeChatMember(clientMessageData.getNickname());
            nowLocation = 0L;
        }
        else if (messageType.equals("CHAT")) {
            chatMessage.setChatId(clientMessageData.getChatId());
            chatMessage.setMessage(clientMessageData.getMessage());

            LocalDateTime timeNow = LocalDateTime.now(ZoneId.systemDefault());
            chatMessage.setTime(formatDateTime(timeNow));
            RedisMessageDTO messageNow = RedisMessageDTO.builder()
                    .chatId(chatMessage.getChatId())
                    .content(chatMessage.getMessage())
                    .time(chatMessage.getTime())
                    .nickname(chatMessage.getNickname())
                    .build();
            redisMessageTemplate.opsForList().rightPush("chatroom-" + chatMessage.getChatId(), objectMapper.writeValueAsString(messageNow));
            List<String> chatroomMessageList = redisMessageTemplate.opsForList().range("chatroom-" + chatMessage.getChatId(), 0,-1);
            if (chatroomMessageList.size() >= 50) {
                List<Message> messageList = new ArrayList<>();
                for (String i : chatroomMessageList) {
                    RedisMessageDTO redisMessageDTO = objectMapper.readValue(i, RedisMessageDTO.class);
                    Message messageI = Message.builder()
                            .chatId(redisMessageDTO.getChatId())
                            .content(redisMessageDTO.getContent())
                            .nickname(redisMessageDTO.getNickname())
                            .time(redisMessageDTO.getTime())
                            .build();
                    messageList.add(messageI);
                }
                messageRepository.saveAll(messageList);
                redisMessageTemplate.delete("chatroom-" + chatMessage.getChatId());
            }
        }
        if (!messageType.equals("LOAD")) {
            chatMessage.setNickname(clientMessageData.getNickname());
            chatMessage.setType(clientMessageData.getType());
            chatMessage.setChatroomTitle(clientMessageData.getChatroomTitle());

            // 현지 시각을 불러와서 형식에 맞게 집어넣는다.
//            LocalDateTime timeNow = LocalDateTime.now(ZoneId.systemDefault());
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            chatMessage.setTime(formatDateTime(timeNow));
            System.out.println(objectMapper.writeValueAsString(chatMessage));
            chatService.sendMessage(chatMessage.getNickname(), objectMapper.writeValueAsString(chatMessage));
        }
    }

    @KafkaListener(topics = "chat-message")
    protected void receiveTextMessage(String message) throws Exception {
        System.out.println("------- 3 --------");
        System.out.println(message);
        System.out.println("------- 3 --------");
        ChatMessageData chatMessage = objectMapper.readValue(message, ChatMessageData.class);
        System.out.println(chatMessage.getChatId());
        CurrentChatrooms currentChatroom = currentChatroomsMap.get(chatMessage.getChatId());
        currentChatroom.chatroomSendMessage(message);
    }

}