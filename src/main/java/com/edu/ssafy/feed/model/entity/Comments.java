package com.edu.ssafy.feed.model.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "comments")
public class Comments {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "c_content")
    private String content;

    @Column(name = "c_created_at")
    private LocalDateTime createdAt;

    @Column(name = "c_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "c_is_deleted")
    private boolean isDeleted;

    @Column(name = "c_nickname")
    private String nickname;

}
