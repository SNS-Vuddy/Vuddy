package com.buddy.model.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonRes {

    private int status;
    private String message;

    public CommonRes(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
