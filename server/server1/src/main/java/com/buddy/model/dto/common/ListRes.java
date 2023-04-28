package com.buddy.model.dto.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListRes<T> extends CommonRes {

    List<T> data;

    public ListRes(int status, String message, List<T> data) {
        super(status, message);
        this.data = data;
    }
}
