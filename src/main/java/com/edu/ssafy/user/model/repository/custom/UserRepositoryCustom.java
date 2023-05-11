package com.edu.ssafy.user.model.repository.custom;

import com.edu.ssafy.user.model.dto.UserAlarmDto;

public interface UserRepositoryCustom {
    String existsByMyUserNicknameAndTargetUserNickname(String myNickname, String userNickname);

    UserAlarmDto findUserAndAlarm(String nickname);
}
