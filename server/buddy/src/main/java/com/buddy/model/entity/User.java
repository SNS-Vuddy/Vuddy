package com.buddy.model.entity;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Max(value = 20, message = "닉네임은 20자 이하로 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "비밀번호가 입력되지 않았습니다.")
    private String password;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "status_message")
    private String statusMessage;
}
