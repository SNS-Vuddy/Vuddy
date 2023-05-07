package com.edu.ssafy.friend.model.entity;

import com.edu.ssafy.friend.model.entity.enums.UserRoll;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Size(max = 20, message = "닉네임은 20자 이하로 입력해주세요.")
    @NotEmpty(message = "닉네임이 입력되지 않았습니다.")
    @Column(name = "u_nickname")
    private String nickname;

    @Column(name = "u_email")
    private String email;

    @NotEmpty(message = "비밀번호가 입력되지 않았습니다.")
    @Column(name = "u_password")
    private String password;

    @Column(name = "u_profile_img")
    private String profileImage;

    @Column(name = "u_status_message")
    private String statusMessage;

    @Column(name = "u_roll")
    @Enumerated(EnumType.STRING)
    private UserRoll userRoll;

    @Column(name = "u_withdrawal_at")
    private LocalDateTime withdrawalAt;

    @Column(name = "u_is_withdrawal")
    private boolean isWithdrawal;

    // 생성 메서드
    public static User createNormalUser(String nickname, String password, String profileImage, String statusMessage) {
        User user = new User();
        user.nickname = nickname;
        user.password = password;
        user.profileImage = profileImage;
        user.statusMessage = statusMessage;
        user.userRoll = UserRoll.NORMAL_USER;
        return user;
    }

    public static User createUserWithEmail(String nickname, String email, String password) {
        User user = new User();
        user.nickname = nickname;
        user.email = email;
        user.password = password;
        return user;
    }

    public void updateStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

}
