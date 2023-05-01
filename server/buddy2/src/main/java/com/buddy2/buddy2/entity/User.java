package com.buddy2.buddy2.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "u_nickname")
    private String nickname;

    @Column(name = "u_email")
    private String email;

    @Column(name = "u_password")
    private String password;

    @Column(name = "u_profile_img")
    private String profileImg;

    @Column(name = "u_status_message")
    private String statusMessage;

    @Column(name = "u_created_at")
    private String createdAt;

    @Column(name = "u_withdrawal_at")
    private String withdrawalAt;

    @Column(name = "u_is_withdrawal")
    private Boolean isWithdrawal;

}
