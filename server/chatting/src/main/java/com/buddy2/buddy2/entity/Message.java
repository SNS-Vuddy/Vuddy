package com.buddy2.buddy2.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "message")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msg_id")
    private Long msgId;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "m_content")
    private String content;

    @Column(name = "m_nickname")
    private String nickname;

    @Column(name = "m_time")
    private String time;

}
