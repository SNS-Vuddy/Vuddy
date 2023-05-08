package com.edu.ssafy.feed.model.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "feed_pictures")
public class FeedPictures {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_picture_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Column(name = "fp_img_url")
    private String imgUrl;
}
