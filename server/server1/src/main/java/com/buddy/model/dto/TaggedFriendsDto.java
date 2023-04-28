package com.buddy.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaggedFriendsDto {

    private Long taggedFriendId;
    private String nickname;
}
