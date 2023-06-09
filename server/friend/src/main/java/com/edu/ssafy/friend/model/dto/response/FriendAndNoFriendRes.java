package com.edu.ssafy.friend.model.dto.response;

import com.edu.ssafy.friend.model.dto.NicknameImgDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendAndNoFriendRes {

    private List<NicknameImgDto> friends;
    private List<NicknameImgDto> noFriends;
}
