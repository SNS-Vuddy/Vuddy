package com.buddy.controller;

import com.buddy.model.dto.common.CommonRes;
import com.buddy.model.dto.common.SingleRes;
import com.buddy.model.dto.request.SignupReq;
import com.buddy.model.dto.response.FindUserRes;
import com.buddy.model.entity.User;
import com.buddy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/api/v1/user/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonRes signup(@RequestBody @Valid SignupReq signupReq){
        User signupUser = User.createUser(signupReq.getNickname(), passwordEncoder.encode(signupReq.getPassword()), signupReq.getProfileImage(), signupReq.getStatusMessage());

        userService.join(signupUser);

        return new CommonRes(201, "회원가입 성공");
    }

    @GetMapping("/api/v1/user/{userNickname}")
    public CommonRes getUserInfo(@PathVariable String userNickname) {
        User user = userService.findByNickname(userNickname);
        FindUserRes findUserRes = new FindUserRes(user.getNickname(), user.getProfileImage(), user.getStatusMessage());
        return new SingleRes<>(200, "유저 정보 조회 성공", findUserRes);
    }

//    유저 프로필이미지 수정은 추후에 구현
//    @PutMapping("/api/v1/user/profile/edit/img/{userNickname}")
//    public void editProfileImage(@PathVariable String userNickname, @RequestBody String profileImage) {
//        User user = userService.findByNickname(userNickname);
//        user.setProfileImage(profileImage);
//    }

    @PutMapping("/api/v1/user/profile/edit/status/{userNickname}")
    public CommonRes editStatusMessage(@PathVariable String userNickname, @RequestBody String statusMessage) {
//        User user = userService.findByNickname(userNickname);
        userService.changeUserStatusMessage(userNickname, statusMessage);
        return new CommonRes(200, "상태메세지 수정 성공");
    }
}
