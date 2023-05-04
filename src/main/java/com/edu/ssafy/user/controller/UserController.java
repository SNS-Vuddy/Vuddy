package com.edu.ssafy.user.controller;

import com.edu.ssafy.user.model.dto.common.CommonRes;
import com.edu.ssafy.user.model.dto.common.SingleRes;
import com.edu.ssafy.user.model.dto.response.UserFeedsSummaryRes;
import com.edu.ssafy.user.model.dto.response.UserProfileWithFeedsRes;
import com.edu.ssafy.user.model.entity.User;
import com.edu.ssafy.user.model.service.UserService;
import com.edu.ssafy.user.util.NicknameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//     내 프로필, 피드들 조회
    @GetMapping("/profile")
    public CommonRes getMyInfoAndFeeds(@RequestHeader("x-forwarded-for-nickname") String encodeNickname) {
        String nickname = NicknameUtil.decodeNickname(encodeNickname);
        User user = userService.findById(nickname);
        UserFeedsSummaryRes myUserWithFeedsDto = userService.findUserAndFeeds(user);
        return new SingleRes<>(200, "유저 정보 조회 성공", myUserWithFeedsDto);
    }

    // 특정 유저 프로필, 피드들 조회
    @GetMapping("/profile/{targetUserNickname}")
    public CommonRes getUserInfoAndFeeds(@RequestHeader("x-forwarded-for-nickname") String encodeNickname, @PathVariable String targetUserNickname) {
        String userNickname = NicknameUtil.decodeNickname(encodeNickname);

        UserProfileWithFeedsRes userProfileWithFeedsRes = userService.findUsersWithFriendStatus(userNickname, targetUserNickname);

        return new SingleRes<>(200, "유저 정보 조회 성공", userProfileWithFeedsRes);
    }


}
