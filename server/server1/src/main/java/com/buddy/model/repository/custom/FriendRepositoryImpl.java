package com.buddy.model.repository.custom;

import com.buddy.model.dto.FriendAndNoFriendDto;
import com.buddy.model.entity.User;
import com.buddy.model.entity.enums.UserFriendStatus;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.buddy.model.entity.QUser.user;
import static com.buddy.model.entity.QUserFriends.userFriends;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public FriendAndNoFriendDto findMyFriendAndNoFriend(String myNickname, String nickname) {

        List<Tuple> results = queryFactory
                .select(user, userFriends.status)
                .from(user)
                .leftJoin(userFriends)
                .on(userFriends.requestUser.eq(user).or(userFriends.receiveUser.eq(user)))
                .where(user.nickname.like("%" + nickname + "%")
                        .and(user.nickname.ne(myNickname))
                        .and((userFriends.requestUser.nickname.eq(myNickname)
                                .or(userFriends.receiveUser.nickname.eq(myNickname)))))
                .fetch();

        for (Tuple result : results) {
            System.out.println("result.get() = " + Objects.requireNonNull(result.get(user)).getNickname());
        }

        List<User> friends = results.stream()
                .filter(tuple -> tuple.get(userFriends.status) == UserFriendStatus.ACCEPTED)
                .map(tuple -> tuple.get(user))
                .collect(Collectors.toList());

        List<User> noFriends = results.stream()
                .filter(tuple -> tuple.get(userFriends.status) != UserFriendStatus.ACCEPTED)
                .map(tuple -> tuple.get(user))
                .collect(Collectors.toList());

        return new FriendAndNoFriendDto(friends, noFriends);
    }
}
