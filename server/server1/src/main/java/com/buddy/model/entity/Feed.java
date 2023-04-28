package com.buddy.model.entity;

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

    @Column(name = "f_content")
    private String content;

    @Column(name = "f_location")
    private String location;

    @Column(name = "f_created_at")
    private LocalDateTime createdAt;

    @Column(name = "f_updated_at")
    private LocalDateTime updatedAt;

    public void updateContentAndLocation(String content, String location) {
        this.content = content;
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }

}
