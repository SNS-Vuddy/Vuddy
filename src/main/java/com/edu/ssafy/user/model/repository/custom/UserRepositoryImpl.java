package com.edu.ssafy.user.model.repository.custom;

import com.edu.ssafy.user.model.dto.UserAlarmDto;
import com.edu.ssafy.user.model.entity.enums.UserFriendStatus;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.edu.ssafy.user.model.entity.QUser.user;
import static com.edu.ssafy.user.model.entity.QUserFriends.userFriends;

@Repository
@RequiredArgsConstructor

public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public String existsByMyUserNicknameAndTargetUserNickname(String myUserNickname, String targetUserNickname) {

        Tuple tuple = queryFactory
                .select(user, userFriends.status)
                .from(user)
                .join(userFriends).on(userFriends.requestUser.eq(user))
                .where((user.nickname.eq(myUserNickname).and(userFriends.receiveUser.nickname.eq(targetUserNickname)))
                        .or(user.nickname.eq(targetUserNickname).and(userFriends.receiveUser.nickname.eq(myUserNickname))))
                .fetchFirst();

        // 없는경우 No를 리턴
        if (tuple == null) {
            return "No";
        }

        return (tuple.get(userFriends.status) == UserFriendStatus.ACCEPTED) ? "Yes" : "Pending";


    }

    @Override
    public UserAlarmDto findUserAndAlarm(String nickname) {

        Tuple tuple = queryFactory
                .select(user, userFriends.status)
                .from(user)
                .leftJoin(userFriends).on((userFriends.receiveUser.eq(user)).and(userFriends.status.eq(UserFriendStatus.PENDING)))
                .where(user.nickname.eq(nickname))
                .fetchFirst();

        return new UserAlarmDto(tuple.get(user), tuple.get(userFriends.status) != null);

    }

}
