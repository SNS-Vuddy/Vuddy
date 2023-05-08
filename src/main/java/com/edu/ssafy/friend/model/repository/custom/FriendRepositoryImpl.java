package com.edu.ssafy.friend.model.repository.custom;

import com.edu.ssafy.friend.model.dto.NicknameImgDto;
import com.edu.ssafy.friend.model.dto.NicknameImgStatusDto;
import com.edu.ssafy.friend.model.dto.response.FriendAndNoFriendRes;
import com.edu.ssafy.friend.model.entity.User;
import com.edu.ssafy.friend.model.entity.UserFriends;
import com.edu.ssafy.friend.model.entity.enums.UserFriendStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.edu.ssafy.friend.model.entity.QUser.user;
import static com.edu.ssafy.friend.model.entity.QUserFriends.userFriends;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public FriendAndNoFriendRes findMyFriendAndNoFriend(String myNickname, String nickname) {

        List<NicknameImgStatusDto> results = queryFactory
                .select(Projections.constructor(NicknameImgStatusDto.class, user.nickname, user.profileImage, userFriends.status))
                .from(user)
                .leftJoin(userFriends)
                .on(userFriends.requestUser.eq(user).or(userFriends.receiveUser.eq(user)))
                .where(user.nickname.like("%" + nickname + "%")
                        .and(user.nickname.ne(myNickname))
                        .and((userFriends.requestUser.nickname.eq(myNickname)
                                .or(userFriends.receiveUser.nickname.eq(myNickname)))))
                .orderBy(user.nickname.asc())
                .fetch();

        List<NicknameImgDto> friends = results.stream()
                .filter(dto -> dto.getStatus() == UserFriendStatus.ACCEPTED)
                .map(dto -> new NicknameImgDto(dto.getNickname(), dto.getProfileImage()))
                .collect(Collectors.toList());

        List<NicknameImgDto> noFriends = results.stream()
                .filter(dto -> dto.getStatus() != UserFriendStatus.ACCEPTED)
                .map(dto -> new NicknameImgDto(dto.getNickname(), dto.getProfileImage()))
                .collect(Collectors.toList());

        return new FriendAndNoFriendRes(friends, noFriends);
    }

    @Override
    public boolean existsByRequestUserAndReceiveUserAndStatusIsOrReceiveUserAndRequestUserAndStatusIs(User requester, User receiver, UserFriendStatus status1, User receiver2, User requester2, UserFriendStatus status2) {

        Long result = queryFactory
                .select(userFriends.id)
                .from(userFriends)
                .where((userFriends.requestUser.eq(requester).and(userFriends.receiveUser.eq(receiver)).and(userFriends.status.eq(status1)))
                        .or(userFriends.requestUser.eq(receiver2).and(userFriends.receiveUser.eq(requester2)).and(userFriends.status.eq(status2))))
                .limit(1)
                .fetchOne();

        return result != null;

    }

    @Override
    public UserFriends findFriendRelation(User requester, User receiver, UserFriendStatus status1, User receiver2, User requester2, UserFriendStatus status2) {

        return queryFactory
                .select(userFriends)
                .from(userFriends)
                .where((userFriends.requestUser.eq(requester).and(userFriends.receiveUser.eq(receiver)).and(userFriends.status.eq(status1)))
                        .or(userFriends.requestUser.eq(receiver2).and(userFriends.receiveUser.eq(requester2)).and(userFriends.status.eq(status2))))
                .limit(1)
                .fetchOne();
    }
}
