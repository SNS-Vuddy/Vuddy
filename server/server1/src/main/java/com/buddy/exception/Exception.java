package com.buddy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Exception {
    MY_EXCEPTION(400, "a는 안된다 닝겐.");

    private final int code;
    private final String message;
}
