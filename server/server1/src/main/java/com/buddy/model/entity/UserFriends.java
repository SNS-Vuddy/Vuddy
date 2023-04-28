package com.buddy.model.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserFriends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_friend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_user_id")
    private User requstUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id")
    private User receiveUser;

    @Column(name = "uf_status")
    private String status;

}
