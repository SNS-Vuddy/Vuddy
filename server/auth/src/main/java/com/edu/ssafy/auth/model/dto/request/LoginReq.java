package com.edu.ssafy.auth.model.dto.request;

import lombok.Data;

@Data
public class LoginReq {

        private String nickname;

        private String password;
}
