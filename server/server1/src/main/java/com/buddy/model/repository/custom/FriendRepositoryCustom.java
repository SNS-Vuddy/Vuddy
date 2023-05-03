package com.buddy.model.repository.custom;

import com.buddy.model.dto.FriendAndNoFriendDto;

public interface FriendRepositoryCustom {

    FriendAndNoFriendDto findMyFriendAndNoFriend(String myNickname, String nickname);
}
