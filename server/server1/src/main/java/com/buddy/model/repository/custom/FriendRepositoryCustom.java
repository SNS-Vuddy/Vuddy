package com.buddy.model.repository.custom;

import com.buddy.model.dto.response.FriendAndNoFriendRes;

public interface FriendRepositoryCustom {

    FriendAndNoFriendRes findMyFriendAndNoFriend(String myNickname, String nickname);
}
