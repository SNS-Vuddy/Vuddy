package com.edu.ssafy.friend.model.entity;

import com.edu.ssafy.friend.model.entity.enums.UserFriendStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFriends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_friend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requestUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiveUser;

    @Column(name = "uf_status")
    @Enumerated(EnumType.STRING)
    private UserFriendStatus status;

    public static UserFriends createRequest(User requstUser, User receiveUser) {
        return UserFriends.builder()
                .requestUser(requstUser)
                .receiveUser(receiveUser)
                .status(UserFriendStatus.PENDING) // 혹은 초기 상태값 설정
                .build();
    }

    public void accept() {
        this.status = UserFriendStatus.ACCEPTED;
    }

    public void deny() {
        this.status = UserFriendStatus.DENIED;
    }
}
