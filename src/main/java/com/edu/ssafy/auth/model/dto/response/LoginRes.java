package com.edu.ssafy.auth.model.dto.response;

import com.edu.ssafy.auth.model.dto.common.CommonRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRes extends CommonRes {

    private String accessToken;
    private String refreshToken;
    private String profileImage;

    public LoginRes(int status, String message, String accessToken, String refreshToken, String profileImage) {
        super(status, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.profileImage = profileImage;
    }
}
