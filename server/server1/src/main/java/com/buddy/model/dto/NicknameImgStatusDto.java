package com.buddy.model.dto;

import com.buddy.model.entity.enums.UserFriendStatus;
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
