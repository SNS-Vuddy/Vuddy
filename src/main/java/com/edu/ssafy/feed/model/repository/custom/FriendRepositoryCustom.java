package com.edu.ssafy.feed.model.repository.custom;

import java.util.List;

public interface FriendRepositoryCustom {

    List<Long> findAllFriendsId(Long userId);
}
