package com.buddy2.buddy2.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "user_chatroom")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserChatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chatroom_id")
    private Long userChatroomId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "chat_id")
    private Long chatId;

}
