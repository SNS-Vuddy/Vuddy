package com.edu.ssafy.friend.model.dto;

import com.edu.ssafy.friend.model.entity.enums.UserFriendStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NicknameImgStatusDto {

    private String nickname;
    private String profileImage;
    private UserFriendStatus status;
}
