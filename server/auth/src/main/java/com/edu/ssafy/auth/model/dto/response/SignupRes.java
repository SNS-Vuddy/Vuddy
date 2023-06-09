package com.edu.ssafy.auth.model.dto.response;

import com.edu.ssafy.auth.model.dto.common.CommonRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRes extends CommonRes {

    private String accessToken;
    private String refreshToken;

    public SignupRes(int status, String message, String accessToken, String refreshToken) {
        super(status, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
