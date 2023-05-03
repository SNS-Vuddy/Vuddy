package com.buddy.model.dto;

import com.buddy.model.entity.User;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Setter
public class UserWithFriendDto {
    private User myUser;
    private User targetUser;
    private String isFriend;

    public User getMyUser() {
        return myUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    @JsonGetter("isFriend")
    public String getIsFriend() {
        return isFriend;
    }
}
