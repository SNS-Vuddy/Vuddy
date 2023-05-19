package com.buddy2.buddy2.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "chatroom")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "c_last_chat")
    private String lastChat;

    @Column(name = "c_nickname")
    private String nickname;

    @Column(name = "c_time")
    private String time;

    @Column(name = "c_title")
    private String title;

}
