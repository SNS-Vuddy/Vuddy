package com.buddy.model.service;

import com.buddy.model.entity.User;
import com.buddy.model.entity.UserFriends;
import com.buddy.model.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final FriendRepository friendRepository;

    @Transactional
    public void requestAddFriend(User requester, User receiver) {

        boolean isExist = friendRepository.existsByRequestUserAndReceiveUser(requester, receiver);

        if (isExist) {
            throw new IllegalStateException("이미 친구 추가 요청을 보냈습니다.");
        } else {
            UserFriends userFriends = UserFriends.createRequest(requester, receiver);
            friendRepository.save(userFriends);
        }

    }
}
