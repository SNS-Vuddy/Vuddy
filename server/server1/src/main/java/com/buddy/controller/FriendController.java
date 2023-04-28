package com.buddy.controller;

import com.buddy.model.dto.common.CommonRes;
import com.buddy.model.entity.User;
import com.buddy.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final UserRepository userRepository;

    //친구 추가 요청
    @GetMapping("/add")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public CommonRes addFriend(@RequestHeader("Authorization") String token, @RequestBody String friendNickname) {

        User requester = userRepository.findByNickname(friendNickname);
        User receiver = userRepository.findByNickname(friendNickname);

        return new CommonRes(201, "친구 추가 요청 성공");
    }

}
