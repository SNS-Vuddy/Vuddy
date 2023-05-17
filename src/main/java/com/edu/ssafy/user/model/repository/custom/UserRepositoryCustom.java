package com.edu.ssafy.user.model.repository.custom;

import com.edu.ssafy.user.model.dto.UserAlarmDto;
import com.edu.ssafy.user.model.entity.User;

public interface UserRepositoryCustom {
    String existsByMyUserNicknameAndTargetUserNickname(User myNickname, User userNickname);

    UserAlarmDto findUserAndAlarm(String nickname);
}
