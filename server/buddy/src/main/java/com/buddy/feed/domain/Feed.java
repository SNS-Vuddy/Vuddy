package com.buddy.feed.domain;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Slf4j
@ToString
@Table(name="feed")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "content")
    private String content;

    @Column(name = "location")
    private String location;

    // 기타 필요한 필드

    // 생성자, getter, setter 등
}
