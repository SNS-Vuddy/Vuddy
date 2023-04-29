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
        UserFriends userFriends = UserFriends.createRequest(requester, receiver);
        System.out.println("=================================================================");
        System.out.println("userFriends.getRequstUser().getNickname() = " + userFriends.getRequstUser().getNickname());
        System.out.println("userFriends.getReceiveUser().getNickname() = " + userFriends.getReceiveUser().getNickname());
        System.out.println("=================================================================");

        friendRepository.save(userFriends);
    }
}
