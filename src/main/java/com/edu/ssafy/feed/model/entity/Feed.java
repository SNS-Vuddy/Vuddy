package com.edu.ssafy.feed.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "feed")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feed {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "f_nickname")
    private String nickname;

    @Column(name = "f_title")
    private String title;

    @Column(name = "f_content")
    private String content;

    @Column(name = "f_location")
    private String location;

    @Column(name = "f_main_img")
    private String mainImg;

    @Column(name = "f_created_at")
    private LocalDateTime createdAt;

    @Column(name = "f_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "f_is_deleted")
    private boolean isDeleted;

    public void updateContentAndLocation(String title, String content, String location) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }

    public void addMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

}
