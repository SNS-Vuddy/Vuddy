package com.edu.ssafy.feed.model.service;

import com.edu.ssafy.feed.model.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final FriendRepository friendRepository;

    public List<Long> findAllFriendsId(Long userId) {
        return friendRepository.findAllFriendsId(userId);
    }
}
