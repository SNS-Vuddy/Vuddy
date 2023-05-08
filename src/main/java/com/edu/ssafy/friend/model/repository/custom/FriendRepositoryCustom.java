package com.edu.ssafy.friend.model.repository.custom;

import com.edu.ssafy.friend.model.dto.response.FriendAndNoFriendRes;

public interface FriendRepositoryCustom {

    FriendAndNoFriendRes findMyFriendAndNoFriend(String myNickname, String nickname);

}
