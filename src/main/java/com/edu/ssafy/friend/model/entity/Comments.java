package com.edu.ssafy.friend.model.entity;

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

    public static Comments createComment(User user, Feed feed, String content) {
        Comments comments = new Comments();
        comments.user = user;
        comments.feed = feed;
        comments.content = content;
        comments.createdAt = LocalDateTime.now();
        comments.updatedAt = LocalDateTime.now();
        return comments;
    }

}
