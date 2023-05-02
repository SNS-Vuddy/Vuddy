package com.buddy.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SignupReq {

    @NotEmpty(message = "닉네임이 입력되지 않았습니다.")
    private String nickname;

    @NotEmpty(message = "비밀번호가 입력되지 않았습니다.")
    private String password;

    private String profileImage;

    private String statusMessage;
}
