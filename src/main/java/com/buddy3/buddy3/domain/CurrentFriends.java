package com.buddy3.buddy3.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class CurrentFriends {
    private ConcurrentHashMap<String, WebSocketSession> friendsMap = new ConcurrentHashMap<>();
    private String nickname;
    private WebSocketSession session;

    public void addFriends(String nickname, WebSocketSession session) {
        friendsMap.put(nickname, session);
    }

    public void removeFriends(String nickname) {
        if (friendsMap.get(nickname) != null) {
            friendsMap.remove(nickname);
        }
    }

    public void currentFriendsSendMessage(String message) throws Exception {
        for (Map.Entry<String, WebSocketSession> entry : friendsMap.entrySet()) {
            try {
                synchronized (entry.getValue()) {
                    entry.getValue().sendMessage(new TextMessage(message));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Builder
    public CurrentFriends(String nickname, WebSocketSession session) {
        this.nickname = nickname;
        this.session = session;
        this.friendsMap.put(nickname, session);
    }
}
