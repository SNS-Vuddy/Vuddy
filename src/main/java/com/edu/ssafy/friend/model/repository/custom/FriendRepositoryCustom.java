package com.edu.ssafy.friend.model.repository.custom;

import com.edu.ssafy.friend.model.dto.response.FriendAndNoFriendRes;
import com.edu.ssafy.friend.model.entity.User;
import com.edu.ssafy.friend.model.entity.UserFriends;
import com.edu.ssafy.friend.model.entity.enums.UserFriendStatus;

public interface FriendRepositoryCustom {

    FriendAndNoFriendRes findMyFriendAndNoFriend(String myNickname, String nickname);

    boolean existsByRequestUserAndReceiveUserAndStatusIsOrReceiveUserAndRequestUserAndStatusIs(User requester, User receiver, UserFriendStatus status1, User receiver2, User requester2, UserFriendStatus status2);

    UserFriends findFriendRelation(User requester, User receiver, UserFriendStatus status1, User receiver2, User requester2, UserFriendStatus status2);

}
