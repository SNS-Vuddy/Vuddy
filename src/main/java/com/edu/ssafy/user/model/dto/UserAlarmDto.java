package com.edu.ssafy.user.model.dto;

import com.edu.ssafy.user.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAlarmDto {

    private User user;
    private boolean hasNewAlarm;
}
