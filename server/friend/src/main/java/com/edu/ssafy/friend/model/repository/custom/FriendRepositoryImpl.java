package com.edu.ssafy.friend.model.repository.custom;

import com.edu.ssafy.friend.model.dto.AllFriendDto;
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

        User myUser = queryFactory
                .selectFrom(user)
                .where(user.nickname.eq(myNickname))
                .fetchOne();

        List<NicknameImgStatusDto> results = queryFactory
                .select(Projections.constructor(NicknameImgStatusDto.class, user.nickname, user.profileImage, userFriends.status))
                .from(user)
                .leftJoin(userFriends)
                .on(
                        (user.eq(userFriends.requestUser).and(userFriends.receiveUser.eq(myUser)))
                        .or(user.eq(userFriends.receiveUser).and(userFriends.requestUser.eq(myUser)))
                )
                .where(
                        (user.nickname.contains(nickname))
                        .and(user.id.ne(myUser.getId()))
                )
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

    @Override
    public List<AllFriendDto> findAllByReceiveUserAndStatusIs(User user, UserFriendStatus userFriendStatus) {
        return queryFactory
                .select(Projections.constructor(AllFriendDto.class, userFriends.requestUser.nickname, userFriends.requestUser.profileImage))
                .from(userFriends)
                .where(userFriends.receiveUser.eq(user).and(userFriends.status.eq(userFriendStatus)))
                .fetch();
    }

    @Override
    public List<AllFriendDto> findAllByRequestUserAndStatusIs(User requester, UserFriendStatus status) {
        return queryFactory
                .select(Projections.constructor(AllFriendDto.class, user.nickname, user.profileImage))
                .from(user)
                .join(userFriends).on((userFriends.receiveUser.eq(user)).or(userFriends.requestUser.eq(user)))
                .where(
                        (userFriends.requestUser.eq(requester).or(userFriends.receiveUser.eq(requester)))
                                .and(userFriends.status.eq(status))
                                .and(user.nickname.ne(requester.getNickname()))
                )
                .orderBy(user.nickname.asc())
                .fetch();
    }
}
