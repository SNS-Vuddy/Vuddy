package com.edu.ssafy.user.model.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleRes<T> extends CommonRes {

    T data;

    public SingleRes(int status, String message, T data) {
        super(status, message);
        this.data = data;
    }
}
