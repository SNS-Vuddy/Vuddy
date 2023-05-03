package com.buddy.model.repository.custom;

import com.buddy.model.dto.UserWithFriendDto;

public interface UserRepositoryCustom {

    UserWithFriendDto findUsersWithFriendStatus(String nickname1, String nickname2);

    String existsByMyUserNicknameAndTargetUserNickname(String myUserNickname, String targetUserNickname);
}
