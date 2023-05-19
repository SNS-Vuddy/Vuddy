package com.buddy2.buddy2.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class CurrentChatrooms {
    private ConcurrentHashMap<String, WebSocketSession> chatroomsMap;
    private String chatroomTitle;
    private Long chatId;

    @Builder
    public CurrentChatrooms(String chatroomTitle,Long chatId, String nickname, WebSocketSession webSocketSession) {
        this.chatroomTitle = chatroomTitle;
        this.chatId = chatId;
        this.chatroomsMap = new ConcurrentHashMap<>();
        this.chatroomsMap.put(nickname, webSocketSession);
    }

    public void addChatMember(String nickname, WebSocketSession webSocketSession) {
        chatroomsMap.put(nickname, webSocketSession);
    }

    public void removeChatMember(String nickname) {
        chatroomsMap.remove(nickname);
    }

    public void chatroomSendMessage(String message) throws Exception {
        for (Map.Entry<String, WebSocketSession> entry : chatroomsMap.entrySet()){
            WebSocketSession session = entry.getValue();
            session.sendMessage(new TextMessage(message));
        }
    }
}
