package com.buddy.model.dto;

import com.buddy.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendAndNoFriendDto {

    private List<User> friends;
    private List<User> noFriends;
}
