package com.edu.ssafy.user.model.repository.custom;

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
        } else if (tuple.get(userFriends.status) == UserFriendStatus.ACCEPTED) {
            return "Yes";
        } else {
            return "Pending";
        }

    }


}
