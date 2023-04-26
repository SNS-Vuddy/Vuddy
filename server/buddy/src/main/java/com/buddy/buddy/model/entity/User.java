package com.buddy.model.entity;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Max(value = 20, message = "닉네임은 20자 이하로 입력해주세요.")
    @NotEmpty(message = "닉네임이 입력되지 않았습니다.")
    @Column(name = "u_nickname")
    private String nickname;

    @NotEmpty(message = "이메일이 입력되지 않았습니다.")
    @Column(name = "u_email")
    private String email;

    @NotEmpty(message = "비밀번호가 입력되지 않았습니다.")
    @Column(name = "u_password")
    private String password;

    @Column(name = "u_profile_img")
    private String profileImage;

    @Column(name = "u_status_message")
    private String statusMessage;

    @Column(name = "u_created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "u_withdrawal_at")
    private LocalDateTime withdrawalAt;

    @Column(name = "u_is_withdrawal")
    private boolean isWithdrawal;

}
