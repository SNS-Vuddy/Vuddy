package com.buddy.model.repository.custom;

import com.buddy.model.dto.UserWithFriendDto;
import com.buddy.model.entity.User;
import com.buddy.model.entity.enums.UserFriendStatus;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.buddy.model.entity.QUser.user;
import static com.buddy.model.entity.QUserFriends.userFriends;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public UserWithFriendDto findUsersWithFriendStatus(String nickname1, String nickname2) {

        Tuple result = queryFactory
                .select(user, userFriends.status)
                .from(user)
                .join(userFriends).on(userFriends.requestUser.eq(user))
                .where((user.nickname.eq(nickname1).and(userFriends.receiveUser.nickname.eq(nickname2)))
                        .or(user.nickname.eq(nickname2).and(userFriends.receiveUser.nickname.eq(nickname1))))
                .fetchOne();

        User user1 = result.get(user);
        User user2 = result.get(userFriends.receiveUser);
        UserFriendStatus status = result.get(userFriends.status);

        return new UserWithFriendDto(user1, user2, status == UserFriendStatus.ACCEPTED);
    }

    @Override
    public boolean existsByMyUserNicknameAndTargetUserNickname(String myUserNickname, String targetUserNickname) {


        long count = queryFactory
                .select(user)
                .from(user)
                .join(userFriends).on(userFriends.requestUser.eq(user))
                .where((user.nickname.eq(myUserNickname).and(userFriends.receiveUser.nickname.eq(targetUserNickname)))
                        .or(user.nickname.eq(targetUserNickname).and(userFriends.receiveUser.nickname.eq(myUserNickname))))
                .fetch()
                .size();

        return count > 0;

    }

}

