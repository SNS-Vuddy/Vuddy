package com.buddy2.buddy2.config;

import com.buddy2.buddy2.data.ChatMessageData;
import com.buddy2.buddy2.domain.CurrentChatrooms;
import com.buddy2.buddy2.dto.*;
import com.buddy2.buddy2.entity.Chatroom;
import com.buddy2.buddy2.entity.User;
import com.buddy2.buddy2.entity.UserChatroom;
import com.buddy2.buddy2.repository.ChatroomRepository;
import com.buddy2.buddy2.repository.MessageRepository;
import com.buddy2.buddy2.repository.UserChatroomRepository;
import com.buddy2.buddy2.repository.UserRepository;
import com.buddy2.buddy2.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.kafka.annotation.KafkaListener;
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

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
    @Autowired
    private RedisTemplate<String, String> redisMessageTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserChatroomRepository userChatroomRepository;


//    public void addValueToRedis (String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }


    ConcurrentHashMap<Long, CurrentChatrooms> currentChatroomsMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> userProfileImgMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, List<Long>> userChatroomListMap = new ConcurrentHashMap<>();
//    Map<String, Integer> chatNumberMap = new ConcurrentHashMap<>();

//    Long chatNum = chatroomRepository.findWithChatId().getChatId();
//    Long chatNumber = chatNum != null? chatNum : 0L;
//    Long chatNumber = 0L;

//    List<CurrentChatrooms> currentChatroomsList = new ArrayList<>();

    Long nowLocation = 0L;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("===================접속=====================");
        System.out.println("접속시간 : " + formatDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul"))));
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
        System.out.println(formatDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul"))) + " | 접속 종료 sessionId : " + session.getId());
        String nickname = sessionUserMap.get(session.getId());
        List<Long> chatroomList = userChatroomListMap.get(nickname);
        if (chatroomList != null) {
            for (Long chatId : chatroomList) {
                CurrentChatrooms currentChatrooms = currentChatroomsMap.get(chatId);
                currentChatrooms.removeChatMember(nickname);
                currentChatroomsMap.put(chatId, currentChatrooms);
            }
            userChatroomListMap.remove(nickname);
        }
        sessionUserMap.remove(session.getId());
        userProfileImgMap.remove(nickname);
//        sessionsList.remove(session);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("===================수신=====================");
        System.out.println(session.getId());
        System.out.println(formatDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul"))) + " | 수신메시지 : " + message.getPayload());
        System.out.println("===================수신=====================");



        ChatMessageData clientMessageData = objectMapper.readValue(message.getPayload(), ChatMessageData.class);

        sessionUserMap.put(session.getId(), clientMessageData.getNickname1());


        MessageSendDTO messageSendDTO = new MessageSendDTO();
        messageSendDTO.setType(clientMessageData.getType());

        MessageSendInnerDTO messageSendInnerDTO = new MessageSendInnerDTO();
        messageSendInnerDTO.setMessage(clientMessageData.getMessage());

        String profileImg = userProfileImgMap.get(clientMessageData.getNickname1());
        if (profileImg == null) {
            User userNow = userRepository.findByNickname(clientMessageData.getNickname1());
            profileImg = userNow.getProfileImg();
            if (profileImg == null) {
                profileImg = "";
            }
            userProfileImgMap.put(clientMessageData.getNickname1(), profileImg);
        }
        messageSendInnerDTO.setProfileImg(profileImg);

        MessageSendJoin messageSendJoin = new MessageSendJoin();
        messageSendJoin.setType(clientMessageData.getType());
        MessageSendJoinInnerDTO messageSendJoinInnerDTO = new MessageSendJoinInnerDTO();

        String messageType = clientMessageData.getType();
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
                    .nickname(clientMessageData.getNickname1())
                    .webSocketSession(session)
                    .build();
            currentChatroomsMap.put(chatNumber,currentChatrooms);

            List<Long> chatroomList = userChatroomListMap.get(clientMessageData.getNickname1());
            if (chatroomList == null) {
                chatroomList = new ArrayList<>();
            }
            chatroomList.add(chatNumber);
            userChatroomListMap.put(clientMessageData.getNickname1(), chatroomList);

            messageSendInnerDTO.setChatId(chatNumber);

            nowLocation = chatNumber;

            LocalDateTime timeNow = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            messageSendInnerDTO.setTime(formatDateTime(timeNow));
            messageSendInnerDTO.setNickname(clientMessageData.getNickname2());

            Chatroom chatroom = Chatroom.builder()
                    .lastChat(messageSendInnerDTO.getMessage())
                    .nickname(clientMessageData.getNickname1())
                    .time(messageSendInnerDTO.getTime())
                    .title(clientMessageData.getNickname2())
                    .build();
            Chatroom savedChatroom = chatroomRepository.save(chatroom);

            User user1 = userRepository.findByNickname(clientMessageData.getNickname1());
            UserChatroom userChatroom1 = UserChatroom.builder()
                    .chatId(savedChatroom.getChatId())
                    .userId(user1.getUserId())
                    .build();
            User user2 = userRepository.findByNickname(clientMessageData.getNickname2());
            UserChatroom userChatroom2 = UserChatroom.builder()
                    .chatId(savedChatroom.getChatId())
                    .userId(user2.getUserId())
                    .build();
            userChatroomRepository.save(userChatroom1);
            userChatroomRepository.save(userChatroom2);
        }
        else if (messageType.equals("JOIN")) {
            User user1 = userRepository.findByNickname(clientMessageData.getNickname1());
            User user2 = userRepository.findByNickname(clientMessageData.getNickname2());
            List<Long> joinChatId = userChatroomRepository.findChatroomId(user1.getUserId(), user2.getUserId());
            CurrentChatrooms currentChatrooms = currentChatroomsMap.get(joinChatId.get(0));
            if (currentChatrooms == null) {
                Chatroom room = chatroomRepository.findByChatId(joinChatId.get(0));
                if (Objects.equals(room.getChatId(), joinChatId.get(0))) {
                    currentChatrooms = CurrentChatrooms.builder()
                            .chatId(room.getChatId())
                            .chatroomTitle(room.getTitle())
                            .nickname(clientMessageData.getNickname1())
                            .webSocketSession(session)
                            .build();
                    currentChatroomsMap.put(room.getChatId(), currentChatrooms);
                    List<Long> chatroomList = new ArrayList<>();
                    chatroomList.add(room.getChatId());
                    userChatroomListMap.put(clientMessageData.getNickname1(), chatroomList);
                    nowLocation = room.getChatId();
                }
            } else {
                currentChatrooms.addChatMember(clientMessageData.getNickname1(), session);
                currentChatroomsMap.replace(joinChatId.get(0), currentChatrooms);
                nowLocation = joinChatId.get(0);
            }
            if (currentChatrooms == null) {
                log.warn("없는 방에 JOIN 요청");
                return;
            }




            LocalDateTime timeNow = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            messageSendJoinInnerDTO.setChatId(joinChatId.get(0));
            messageSendJoinInnerDTO.setNickname(clientMessageData.getNickname2());


            String redisKey = "chatroom-" + messageSendJoinInnerDTO.getChatId();
            List<MessageSendInnerDTO> messageList = new ArrayList<>();
            List<String> redisMessageList = redisMessageTemplate.opsForList().range(redisKey,-20,-1);
            if (redisMessageList == null) {
                redisMessageList = new ArrayList<>();
            }
            for (String messageStr : redisMessageList) {
                messageList.add(objectMapper.readValue(messageStr, MessageSendInnerDTO.class));
            }

            messageSendJoinInnerDTO.setMessageList(messageList);
            messageSendJoin.setData(messageSendJoinInnerDTO);

        }
        else if (messageType.equals("LOAD")) {
            MessageSendLoadDTO messageSendLoadDTO = new MessageSendLoadDTO();
            messageSendLoadDTO.setType(clientMessageData.getType());
            List<Chatroom> chatroomList = chatroomRepository.findWithNickname(clientMessageData.getNickname1());
            User userNow = userRepository.findByNickname(clientMessageData.getNickname1());
            if (userNow == null) {
                log.warn("없는 유저입니다. : {}", clientMessageData.getNickname1());
            }
            else {
                List<MessageSendLoadInner> loadInnerList = new ArrayList<>();
                for (Chatroom chatroom : chatroomList) {
                    User oppositeUser = userRepository.findByUserId(userChatroomRepository.findwithChatIdAndUserId(chatroom.getChatId(), userNow.getUserId()));
                    MessageSendLoadInner messageSendLoadInner = null;
                    if (oppositeUser == null) {
                        messageSendLoadInner = MessageSendLoadInner.builder()
                                .chatId(chatroom.getChatId())
                                .lastChat(chatroom.getLastChat())
                                .nickname(null)
                                .time(chatroom.getTime())
                                .profileImg(null)
                                .build();
                    }
                    else {
                        messageSendLoadInner = MessageSendLoadInner.builder()
                                .chatId(chatroom.getChatId())
                                .lastChat(chatroom.getLastChat())
                                .nickname(oppositeUser.getNickname())
                                .time(chatroom.getTime())
                                .profileImg(oppositeUser.getProfileImg())
                                .build();
                        String lastMessage = redisMessageTemplate.opsForList().index("chatroom-" + chatroom.getChatId(), -1);
                        if (lastMessage != null) {
                            MessageSendInnerDTO lastChatroom = objectMapper.readValue(lastMessage, MessageSendInnerDTO.class);
                            messageSendLoadInner.setLastChat(lastChatroom.getMessage());
                            messageSendLoadInner.setTime(lastChatroom.getTime());
                            messageSendLoadInner.setProfileImg(lastChatroom.getProfileImg());
                        }
                    }
                    loadInnerList.add(messageSendLoadInner);
                }
                messageSendLoadDTO.setInnerList(loadInnerList);
                String sendingMessage = objectMapper.writeValueAsString(messageSendLoadDTO);
                log.info(sendingMessage);
                session.sendMessage(new TextMessage(sendingMessage));

            }

        }
        else if (messageType.equals("EXIT")) {
            CurrentChatrooms currentChatrooms = currentChatroomsMap.get(nowLocation);
            currentChatrooms.removeChatMember(clientMessageData.getNickname1());
            nowLocation = 0L;
        }
        else if (messageType.equals("LEAVE")) {
            CurrentChatrooms currentChatrooms = currentChatroomsMap.get(nowLocation);
            currentChatrooms.removeChatMember(clientMessageData.getNickname1());
            nowLocation = 0L;
        }
        else if (messageType.equals("CHAT")) {

            LocalDateTime timeNow = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            messageSendInnerDTO.setTime(formatDateTime(timeNow));
            messageSendInnerDTO.setChatId(clientMessageData.getChatId());
            messageSendInnerDTO.setNickname(clientMessageData.getNickname1());


            String redisKey = "chatroom-" + messageSendInnerDTO.getChatId();
            redisMessageTemplate.opsForList().rightPush(redisKey, objectMapper.writeValueAsString(messageSendInnerDTO));

        }
        if (!messageType.equals("LOAD")) {

            if (messageType.equals("JOIN")) {

                System.out.println("------- 3 --------");
                System.out.println(objectMapper.writeValueAsString(messageSendJoinInnerDTO));
                System.out.println(objectMapper.writeValueAsString(messageSendJoin));
                System.out.println("------- 3 --------");
                CurrentChatrooms currentChatroom = currentChatroomsMap.get(messageSendJoinInnerDTO.getChatId());
                if (currentChatroom != null) {
                    currentChatroom.chatroomSendMessage(objectMapper.writeValueAsString(messageSendJoin));
                }
                else {
                    log.warn("currentChatroom : null ---> {}", objectMapper.writeValueAsString(messageSendJoin));
                }
            } else {
                messageSendDTO.setData(messageSendInnerDTO);
                System.out.println("------- 3 --------");
                System.out.println(objectMapper.writeValueAsString(messageSendInnerDTO));
                System.out.println(objectMapper.writeValueAsString(messageSendDTO));
                System.out.println("------- 3 --------");
                CurrentChatrooms currentChatroom = currentChatroomsMap.get(messageSendInnerDTO.getChatId());
                if (currentChatroom != null) {
                    currentChatroom.chatroomSendMessage(objectMapper.writeValueAsString(messageSendDTO));
                }
                else {
                    log.warn("currentChatroom : null ---> {}", objectMapper.writeValueAsString(messageSendDTO));
                }
            }

//            chatService.sendMessage(chatMessage.getNickname(), objectMapper.writeValueAsString(chatMessage));
        }
    }

//    @KafkaListener(topics = "chat-message")
//    protected void receiveTextMessage(String message) throws Exception {
//        System.out.println("------- 3 --------");
//        System.out.println(message);
//        System.out.println("------- 3 --------");
//        ChatMessageData chatMessage = objectMapper.readValue(message, ChatMessageData.class);
//        System.out.println(chatMessage.getChatId());
//        CurrentChatrooms currentChatroom = currentChatroomsMap.get(chatMessage.getChatId());
//        currentChatroom.chatroomSendMessage(message);
//    }

}