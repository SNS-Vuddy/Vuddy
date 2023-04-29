package com.buddy.model.entity;

import com.buddy.model.entity.enums.UserFriendStatus;
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
    private User requstUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiveUser;

    @Column(name = "uf_status")
    @Enumerated(EnumType.STRING)
    private UserFriendStatus status;

    public static UserFriends createRequest(User requstUser, User receiveUser) {
        return UserFriends.builder()
                .requstUser(requstUser)
                .receiveUser(receiveUser)
                .status(UserFriendStatus.PENDING) // 혹은 초기 상태값 설정
                .build();
    }

}
