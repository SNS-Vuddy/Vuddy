package com.buddy.model.service;

import com.buddy.exception.FriendRequestNotFoundException;
import com.buddy.model.dto.AllFriendDto;
import com.buddy.model.dto.FriendAndNoFriendDto;
import com.buddy.model.entity.User;
import com.buddy.model.entity.UserFriends;
import com.buddy.model.entity.enums.UserFriendStatus;
import com.buddy.model.repository.FriendRepository;
import com.buddy.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public void requestAddFriend(User requester, User receiver) {

        boolean isExist = friendRepository.existsByRequestUserAndReceiveUserAndStatusIs(requester, receiver, UserFriendStatus.PENDING);

        if (isExist) {
            throw new IllegalStateException("이미 친구 추가 요청을 보냈습니다.");
        } else {
            UserFriends userFriends = UserFriends.createRequest(requester, receiver);
            friendRepository.save(userFriends);
        }
    }

    @Transactional
    public void UpdateFriendStatus(User requester, User receiver, UserFriendStatus status) {
        boolean isExist = friendRepository.existsByRequestUserAndReceiveUserAndStatusIs(requester, receiver, UserFriendStatus.PENDING);

        if (isExist) {
            UserFriends userFriends = friendRepository.findByRequestUserAndReceiveUser(requester, receiver);

            if (status == UserFriendStatus.ACCEPTED) {
                userFriends.accept();
            } else if (status == UserFriendStatus.DENIED) {
                friendRepository.delete(userFriends);
            }
        } else {
            throw new FriendRequestNotFoundException("친구 추가 요청이 없습니다.");
        }
    }

    @Transactional
    public void deleteFriend(User requester, User receiver) {
        boolean isExist = friendRepository.existsByRequestUserAndReceiveUserAndStatusIs(requester, receiver, UserFriendStatus.ACCEPTED);

        if (isExist) {
            UserFriends userFriends = friendRepository.findByRequestUserAndReceiveUser(requester, receiver);
            friendRepository.delete(userFriends);
        } else {
            throw new FriendRequestNotFoundException("친구가 아닙니다.");
        }
    }

    public List<AllFriendDto> findAllFriend(User user) {
        List<AllFriendDto> allFriendDtoList =  friendRepository.findAllByRequestUserAndStatusIs(user, UserFriendStatus.ACCEPTED);
        return allFriendDtoList;
    }

    public FriendAndNoFriendDto searchFriend(String myNickname, String nickname) {
        return friendRepository.findMyFriendAndNoFriend(myNickname, nickname);
    }
}
