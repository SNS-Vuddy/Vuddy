package com.buddy3.buddy3.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_friends")
public class UserFriends {

    @Id
    @Column(name = "user_friend_id")
    private Long uerFriendId;

    @JoinColumn(name = "requester_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User requesterId;

    @JoinColumn(name = "receiver_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User receiverId;

    @Column(name = "uf_status")
    private String status;

    @Column(name = "uf_create_at")
    private String createAt;

}
