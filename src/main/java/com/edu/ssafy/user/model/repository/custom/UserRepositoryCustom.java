package com.edu.ssafy.user.model.repository.custom;

public interface UserRepositoryCustom {
    String existsByMyUserNicknameAndTargetUserNickname(String myNickname, String userNickname);
}
