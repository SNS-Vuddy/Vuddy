package com.buddy2.buddy2.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "user_chatroom")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserChatroom implements Serializable {

    @Id
    @Column(name = "user_chatroom_id")
    private Long userChatroomId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;

    @JoinColumn(name = "chat_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Chatroom chatId;

}
