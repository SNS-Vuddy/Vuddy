package com.edu.ssafy.user.model.repository.custom;

import com.edu.ssafy.user.model.dto.UserAlarmDto;
import com.edu.ssafy.user.model.entity.User;
import com.edu.ssafy.user.model.entity.UserFriends;
import com.edu.ssafy.user.model.entity.enums.UserFriendStatus;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.edu.ssafy.user.model.entity.QUser.user;
import static com.edu.ssafy.user.model.entity.QUserFriends.userFriends;

@Repository
@RequiredArgsConstructor

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public String existsByMyUserNicknameAndTargetUserNickname(User myUser, User targetUser) {

        UserFriends userFriends1 = queryFactory
                .select(userFriends)
                .from(userFriends)
                .where(
                        ((userFriends.requestUser.eq(myUser)).and(userFriends.receiveUser.eq(targetUser)))
                                .or((userFriends.requestUser.eq(targetUser)).and(userFriends.receiveUser.eq(myUser)))
                )
                .fetchFirst();

        if (userFriends1 == null) {
            return "NO";
        } else if (userFriends1.getStatus() == UserFriendStatus.ACCEPTED) {
            return "YES";
        } else if (userFriends1.getRequestUser() == myUser) {
            return "REQUESTED";
        } else if (userFriends1.getReceiveUser() == myUser) {
            return "RECEIVED";
        }

        return "에러발생";
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