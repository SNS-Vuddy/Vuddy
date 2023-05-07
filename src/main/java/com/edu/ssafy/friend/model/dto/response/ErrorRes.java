package com.edu.ssafy.friend.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorRes {
    private int status;
    private String message;

    public ErrorRes(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // getter, setter 생략
}
