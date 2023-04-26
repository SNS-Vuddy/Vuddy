package com.buddy.model.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "feed")
public class Feed {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "f_nickname")
    private String nickname;

    @Column(name = "f_content")
    private String content;

    @Column(name = "f_location")
    private String location;

}
