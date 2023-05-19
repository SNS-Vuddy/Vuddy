package com.edu.ssafy.comment.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tagged_friends")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaggedFriends {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagged_friend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "tf_nickname")
    private String nickname;

}
