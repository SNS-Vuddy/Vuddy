package com.buddy3.buddy3.config;

import com.buddy3.buddy3.data.LocationMessageData;
import com.buddy3.buddy3.distance.GPS;
import com.buddy3.buddy3.domain.CurrentFriends;
import com.buddy3.buddy3.dto.LocationMessageReceive;
import com.buddy3.buddy3.entity.User;
import com.buddy3.buddy3.repository.UserRepository;
import com.buddy3.buddy3.service.LocationService;
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
import java.util.ArrayList;
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

    @Autowired
    private RedisTemplate<String, String> redisLocationTemplate;

    @Autowired
    private GPS gps;


    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    ConcurrentHashMap<String, CurrentFriends> currentUsersCurrentFriendsMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, List<String>> currentUsersFriendsListMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> currentSessionIdsUsersMap = new ConcurrentHashMap<>();



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

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(formatDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul"))) + " | 접속 종료 sessionId : " + session.getId());
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
        System.out.println(formatDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul"))) + "  수신메시지 : " + message.getPayload());
        System.out.println("===================수신=====================");

        LocationMessageReceive locationMessageReceive = objectMapper.readValue(message.getPayload(), LocationMessageReceive.class);

        // 보내온 시간
        LocalDateTime localDateTime = LocalDateTime.parse(locationMessageReceive.getLocalDateTime());



        LocationMessageData locationMessage = new LocationMessageData();
        locationMessage.setNickname(locationMessageReceive.getNickname());
        locationMessage.setLatitude(locationMessageReceive.getLatitude());
        locationMessage.setLongitude(locationMessageReceive.getLongitude());
        locationMessage.setStatus("normal");
        User userNow = userRepository.findByNickname(locationMessage.getNickname());
        if (userNow != null) {
            locationMessage.setImgUrl(userNow.getProfileImg());
        }
        else {
            log.warn("userImg : {} is not exists", locationMessage.getNickname());
            return;
        }


        if (!locationMessage.getNickname().equals("")) {
            if(currentSessionIdsUsersMap.get(session.getId()) == null) {
                currentSessionIdsUsersMap.put(session.getId(), locationMessage.getNickname());
                CurrentFriends currentFriends = currentUsersCurrentFriendsMap.get(locationMessage.getNickname());
                if (currentFriends == null) {
                    currentFriends = CurrentFriends.builder().nickname(locationMessage.getNickname()).session(session).build();
                    currentUsersCurrentFriendsMap.put(locationMessage.getNickname(), currentFriends);
                }

                List<String> friendsNicknameList = currentUsersFriendsListMap.get(locationMessage.getNickname());
                if (friendsNicknameList == null) {
                    User user = userRepository.findByNickname(locationMessage.getNickname());
                    if (user != null) {
                        friendsNicknameList = userRepository.findFriends(user.getUserId());
                        currentUsersFriendsListMap.put(locationMessage.getNickname(), friendsNicknameList);
                    }
                    else {
                        friendsNicknameList = new ArrayList<>();
                    }
                }

                for (String friendNickname : friendsNicknameList) {
                    CurrentFriends friendCurrentFriends = currentUsersCurrentFriendsMap.get(friendNickname);
                    if (friendCurrentFriends != null) {
                        currentFriends.addFriends(friendNickname, friendCurrentFriends.getSession());
                        friendCurrentFriends.addFriends(locationMessage.getNickname(), session);
                        currentUsersCurrentFriendsMap.replace(friendNickname, friendCurrentFriends);

                        List<String> friendFriendsList =  currentUsersFriendsListMap.get(friendNickname);
                        friendFriendsList.add(locationMessage.getNickname());
                        currentUsersFriendsListMap.replace(friendNickname, friendFriendsList);
                    }
                }
            }

            // 서버 시간
            LocalDateTime timeNow = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            locationMessage.setTime(formatDateTime(timeNow));
            boolean hasOffice = redisLocationTemplate.opsForHash().get(locationMessage.getNickname() + "-location", "office") != null;
            boolean hasHome = redisLocationTemplate.opsForHash().get(locationMessage.getNickname() + "-location", "home") != null;
            if (hasOffice || hasHome) {
                List<String> movingList = redisLocationTemplate.opsForList().range(locationMessage.getNickname(), -4,-1);
                if (movingList != null && movingList.size() >= 4) {
                    String[] homeLocation = String.valueOf(redisLocationTemplate.opsForHash().get(locationMessage.getNickname() + "-location", "home")).split(" ");
                    String[] officeLocation = String.valueOf(redisLocationTemplate.opsForHash().get(locationMessage.getNickname() + "-location", "office")).split(" ");
                    double[] homeDistance = new double[4];
                    double[] officeDistance = new double[4];
                    for (int i = 0; i < 4; i++) {
                        String[] movingArr = movingList.get(i).split(" ");
                        if (hasHome) {
                            homeDistance[i] = gps.getDistance(movingArr[0], movingArr[1], homeLocation[0], homeLocation[1]);
                            if (homeDistance[i] < 10D) {
                                locationMessage.setStatus("home");
                                break;
                            }
                        }
                        if (hasOffice) {
                            officeDistance[i] = gps.getDistance(movingArr[0], movingArr[1], officeLocation[0], officeLocation[1]);
                            if (officeDistance[i] < 10D) {
                                locationMessage.setStatus("office");
                                break;
                            }
                        }
                        if (hasHome && hasOffice) {
                            if (i > 0) {
                                if ((homeDistance[i] - homeDistance[i-1]) * (officeDistance[i] - officeDistance[i-1]) > 0) {
                                    break;
                                }
                            }
                            if (i == 3) {
                                if ((homeDistance[3] - homeDistance[0]) < 0) {
                                    locationMessage.setStatus("gotohome");
                                }
                                else if ((officeDistance[3] - officeDistance[0]) < 0) {
                                    locationMessage.setStatus("gotowork");
                                }
                            }
                        }
                    }
                }
            }



            System.out.println("------- 3 --------");
            System.out.println(objectMapper.writeValueAsString(locationMessage));
            System.out.println("------- 3 --------");
            CurrentFriends currentFriends = currentUsersCurrentFriendsMap.get(locationMessage.getNickname());
            System.out.println(currentFriends);
            if (currentFriends != null) {
                currentFriends.currentFriendsSendMessage(objectMapper.writeValueAsString(locationMessage));
            }
            else {
                log.warn("currentFriends : null - {}", locationMessage.getNickname());
            }
//            locationService.sendMessage(locationMessage.getNickname(), objectMapper.writeValueAsString(locationMessage));
            String redisData = locationMessage.getLatitude() + " " + locationMessage.getLongitude() + " " + locationMessage.getTime();
            redisLocationTemplate.opsForList().rightPush(locationMessage.getNickname(), redisData);
//            List<String> locationMessageStringList = redisLocationTemplate.opsForList().range(locationMessage.getNickname(), 0, -1);

            // redis에 추가된 내용 제거
//            redisLocationTemplate.opsForList().rightPop(locationMessage.getNickname());
//            System.out.println(gps.getDistance("36.348094", "127.300050", locationMessage.getLatitude(), locationMessage.getLongitude()));
//            log.warn(redisLocationTemplate.opsForList().rightPop(locationMessage.getNickname()));
        }
    }

    @KafkaListener(topics = "location-message")
    protected void receiveTextMessage(String message) throws Exception {
        System.out.println("------- 3 --------");
        System.out.println(message);
        System.out.println("------- 3 --------");
        LocationMessageData locationMessage = objectMapper.readValue(message, LocationMessageData.class);
        LocalDateTime timeNow = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        locationMessage.setTime(formatDateTime(timeNow));
        CurrentFriends currentFriends = currentUsersCurrentFriendsMap.get(locationMessage.getNickname());
        System.out.println(currentFriends);
        currentFriends.currentFriendsSendMessage(objectMapper.writeValueAsString(locationMessage));
    }
}
