package com.edu.ssafy.feed.model.repository.custom;

import com.edu.ssafy.feed.model.entity.enums.UserFriendStatus;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edu.ssafy.feed.model.entity.QUserFriends.userFriends;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findAllFriendsId(Long userId) {
        List<Tuple> result = queryFactory
                .select(userFriends.receiveUser.id, userFriends.requestUser.id)
                .from(userFriends)
                .where((userFriends.receiveUser.id.eq(userId).or(userFriends.requestUser.id.eq(userId)))
                        .and(userFriends.status.eq(UserFriendStatus.ACCEPTED)))
                .fetch();

        return result.stream()
                .flatMap(tuple -> Stream.of(tuple.get(userFriends.receiveUser.id), tuple.get(userFriends.requestUser.id)))
                .distinct()  // 중복 제거
                .filter(id -> !id.equals(userId))
                .collect(Collectors.toList());
    }

}
