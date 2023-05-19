package com.edu.ssafy.friend.model.service;

import com.edu.ssafy.friend.exception.FriendRequestNotFoundException;
import com.edu.ssafy.friend.model.dto.AllFriendDto;
import com.edu.ssafy.friend.model.dto.response.FriendAndNoFriendRes;
import com.edu.ssafy.friend.model.entity.User;
import com.edu.ssafy.friend.model.entity.UserFriends;
import com.edu.ssafy.friend.model.entity.enums.UserFriendStatus;
import com.edu.ssafy.friend.model.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final FriendRepository friendRepository;

    @Transactional
    public void requestAddFriend(User requester, User receiver) {
        boolean isExist = friendRepository.existsByRequestUserAndReceiveUserAndStatusIsOrReceiveUserAndRequestUserAndStatusIs(requester, receiver, UserFriendStatus.PENDING, receiver, requester, UserFriendStatus.PENDING);

        if (isExist) {
            throw new IllegalStateException("이미 친구 추가 요청을 보냈습니다.");
        } else {
            UserFriends userFriends = UserFriends.createRequest(requester, receiver);
            friendRepository.save(userFriends);
        }
    }

    @Transactional
    public void UpdateFriendStatus(User requester, User receiver, UserFriendStatus status) {
        boolean isExist = friendRepository.existsByRequestUserAndReceiveUserAndStatusIsOrReceiveUserAndRequestUserAndStatusIs(requester, receiver, UserFriendStatus.PENDING, receiver, requester, UserFriendStatus.PENDING);

        if (isExist) {
            UserFriends userFriends = friendRepository.findByRequestUserAndReceiveUser(requester, receiver);

            if (status == UserFriendStatus.ACCEPTED) {
                userFriends.accept();
            } else if (status == UserFriendStatus.DENIED) {
                friendRepository.delete(userFriends);
            }
        } else {
            throw new FriendRequestNotFoundException("친구 추가 요청이 없습니다");
        }
    }

    @Transactional
    public void deleteFriend(User requester, User receiver) {
        UserFriends uf = friendRepository.findFriendRelation(requester, receiver, UserFriendStatus.ACCEPTED, receiver, requester, UserFriendStatus.ACCEPTED);

        if (uf != null) {
            friendRepository.delete(uf);
        } else {
            throw new FriendRequestNotFoundException("친구가 아닙니다.");
        }
    }

    public List<AllFriendDto> findAllFriend(User user) {
        return friendRepository.findAllByRequestUserAndStatusIs(user, UserFriendStatus.ACCEPTED);
    }

    public FriendAndNoFriendRes searchFriend(String myNickname, String nickname) {
        return friendRepository.findMyFriendAndNoFriend(myNickname, nickname);
    }

    public List<AllFriendDto> findFriendRequest(User user) {

        return friendRepository.findAllByReceiveUserAndStatusIs(user, UserFriendStatus.PENDING);
    }
}
