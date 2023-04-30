package com.buddy.controller;

import com.buddy.model.dto.common.CommonRes;
import com.buddy.model.dto.request.RequestAddFriendReq;
import com.buddy.model.entity.User;
import com.buddy.model.service.FriendService;
import com.buddy.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    private final UserService userService;
    private final FriendService friendService;


    //친구 추가 요청
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public ResponseEntity<CommonRes> addFriend(@RequestHeader("Authorization") String token, @RequestBody RequestAddFriendReq req) {
        List<User> users = userService.findAllByNicknameIn(List.of(req.getFriendNickname(), userService.findUserNicknameByToken(token)));

        User requester = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(0) : users.get(1);
        User receiver = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(1) : users.get(0);

        friendService.requestAddFriend(requester, receiver);
        return ResponseEntity.ok(new CommonRes(200, "친구 추가 요청 성공"));
    }

}
