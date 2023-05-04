package com.buddy.model.repository.custom;

import com.buddy.model.dto.response.FriendAndNoFriendRes;
import com.buddy.model.dto.NicknameImgDto;
import com.buddy.model.dto.NicknameImgStatusDto;
import com.buddy.model.entity.enums.UserFriendStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.buddy.model.entity.QUser.user;
import static com.buddy.model.entity.QUserFriends.userFriends;

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
}
