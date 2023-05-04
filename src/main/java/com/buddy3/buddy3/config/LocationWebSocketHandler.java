package com.buddy3.buddy3.config;

import com.buddy3.buddy3.data.LocationMessageData;
import com.buddy3.buddy3.domain.CurrentFriends;
import com.buddy3.buddy3.dto.LocationMessageReceive;
import com.buddy3.buddy3.entity.User;
import com.buddy3.buddy3.repository.UserRepository;
import com.buddy3.buddy3.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocationWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;


    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        redisTemplate.opsForValue();
        return dateTime.format(formatter);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    ConcurrentHashMap<String, CurrentFriends> currentUsersCurrentFriendsMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, List<String>> currentUsersFriendsListMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> currentSessionIdsUsersMap = new ConcurrentHashMap<>();

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

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("접속 종료 sessionId : " + session.getId());
        String nickname = currentSessionIdsUsersMap.get(session.getId());
        if (nickname != null) {
            List<String> friendsNicknameList = currentUsersFriendsListMap.get(nickname);
            if (friendsNicknameList != null) {
                for (String friendNickname : friendsNicknameList) {
                    CurrentFriends friendCurrentFriends = currentUsersCurrentFriendsMap.get(friendNickname);
                    if (friendCurrentFriends != null){
                        friendCurrentFriends.removeFriends(nickname);
                        currentUsersCurrentFriendsMap.replace(friendNickname, friendCurrentFriends);

                        List<String> friendFriendsList = currentUsersFriendsListMap.get(friendNickname);
                        friendFriendsList.remove(nickname);
                        currentUsersFriendsListMap.replace(friendNickname, friendFriendsList);
                    }
                }
                currentUsersFriendsListMap.remove(nickname);
            }
            currentUsersCurrentFriendsMap.remove(nickname);
            currentSessionIdsUsersMap.remove(session.getId());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("===================수신=====================");
        System.out.println(session.getId());
        System.out.println("수신 메시지 : " + message.getPayload());
        System.out.println("===================수신=====================");

        LocationMessageReceive locationMessageReceive = objectMapper.readValue(message.getPayload(), LocationMessageReceive.class);

        LocalDateTime localDateTime = LocalDateTime.parse(locationMessageReceive.getLocalDateTime());

        LocationMessageData locationMessage = new LocationMessageData();
        locationMessage.setNickname(locationMessageReceive.getNickname());
        locationMessage.setLatitude(locationMessageReceive.getLatitude());
        locationMessage.setLongitude(locationMessageReceive.getLongitude());
        locationMessage.setTime(formatDateTime(localDateTime));

        if (locationMessage.getNickname().equals("")) {
            if(currentSessionIdsUsersMap.get(session.getId()) == null) {
                currentSessionIdsUsersMap.put(session.getId(), locationMessage.getNickname());
                CurrentFriends currentFriends = currentUsersCurrentFriendsMap.get(locationMessage.getNickname());
                if (currentFriends == null) {
                    currentFriends = CurrentFriends.builder().nickname(locationMessage.getNickname()).session(session).build();
                    currentUsersCurrentFriendsMap.put(locationMessage.getNickname(), currentFriends);
                }

                List<String> friendsNicknameList = currentUsersFriendsListMap.get(locationMessage.getNickname());
                if (friendsNicknameList == null){
                    User user = userRepository.findByNickname(locationMessage.getNickname());
                    if (user != null) {
                        friendsNicknameList = userRepository.findFriends(user.getUserId());
                        currentUsersFriendsListMap.put(locationMessage.getNickname(), friendsNicknameList);
                    }
                }

                for (String friendNickname : friendsNicknameList) {
                    CurrentFriends friendCurrentFriends = currentUsersCurrentFriendsMap.get(friendNickname);
                    if (friendCurrentFriends != null){
                        currentFriends.addFriends(friendNickname, friendCurrentFriends.getSession());
                        friendCurrentFriends.addFriends(locationMessage.getNickname(), session);
                        currentUsersCurrentFriendsMap.replace(friendNickname, friendCurrentFriends);

                        List<String> friendFriendsList =  currentUsersFriendsListMap.get(friendNickname);
                        friendFriendsList.add(locationMessage.getNickname());
                        currentUsersFriendsListMap.replace(friendNickname, friendFriendsList);
//                friendSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(locationMessage)));
                    }
                }
            }
            locationService.sendMessage(locationMessage.getNickname(), objectMapper.writeValueAsString(locationMessage));
        }
    }

    @KafkaListener(topics = "location-message")
    protected void receiveTextMessage(String message) throws Exception {
        System.out.println("------- 3 --------");
        System.out.println(message);
        System.out.println("------- 3 --------");
        LocationMessageData locationMessage = objectMapper.readValue(message, LocationMessageData.class);
//        LocalDateTime messageTime = LocalDateTime.parse(locationMessage.getTime());
        LocalDateTime timeNow = LocalDateTime.now(ZoneId.systemDefault());
        locationMessage.setTime(formatDateTime(timeNow));
        CurrentFriends currentFriends = currentUsersCurrentFriendsMap.get(locationMessage.getNickname());
//        System.out.println(locationMessage.getChatId());
        currentFriends.currentFriendsSendMessage(objectMapper.writeValueAsString(locationMessage));
    }
}
