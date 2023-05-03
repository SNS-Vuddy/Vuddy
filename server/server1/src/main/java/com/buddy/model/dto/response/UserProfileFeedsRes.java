package com.buddy.model.dto.response;

import com.buddy.model.dto.common.SingleRes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class UserProfileFeedsRes<T> extends SingleRes<T> {

    @Getter
    boolean canISeeFeeds;

    @JsonProperty("isFriend")
    boolean isFriend;

    public UserProfileFeedsRes(int status, String message, T data, boolean canISeeFeeds, boolean isFriend) {
        super(status, message, data);
        this.canISeeFeeds = canISeeFeeds;
        this.isFriend = isFriend;
    }
}
