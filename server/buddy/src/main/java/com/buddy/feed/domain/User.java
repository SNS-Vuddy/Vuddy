package com.buddy.feed.domain;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Slf4j
@ToString
@Table(name="user")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

//    @Column(name = "password")
//    private String password;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "status_msg")
    private String statusMsg;
    // 기타 필요한 필드

    // 생성자, getter, setter 등
}
