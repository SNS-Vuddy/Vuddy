package com.buddy.controller;

import com.buddy.jwt.TokenProvider;
import com.buddy.model.dto.UserWithFeedsDto;
import com.buddy.model.dto.common.CommonRes;
import com.buddy.model.dto.common.SingleRes;
import com.buddy.model.dto.request.LoginReq;
import com.buddy.model.dto.request.SignupReq;
import com.buddy.model.dto.request.UserStatusChangeReq;
import com.buddy.model.dto.response.SignupRes;
import com.buddy.model.entity.User;
import com.buddy.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonRes signup(@RequestBody @Valid SignupReq signupReq){

        User signupUser = User.createNormalUser(signupReq.getNickname(), passwordEncoder.encode(signupReq.getPassword()), signupReq.getProfileImage(), signupReq.getStatusMessage());
        userService.join(signupUser);

        // 액세스 토큰 생성
        String accessToken = userService.createAccessToken(signupUser);

        // 리프레쉬 토큰 생성
        String refreshToken = userService.createRefreshToken(signupUser);

        return new SignupRes(201, "회원가입 성공", accessToken, refreshToken);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonRes> login(@RequestBody @Valid LoginReq loginReq) {
        User loginUser = userService.findByNickname(loginReq.getNickname());
        if (loginUser == null) {
            return new ResponseEntity<>(new CommonRes(400, "이 회원은 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        if (!passwordEncoder.matches(loginReq.getPassword(), loginUser.getPassword())) {
            return new ResponseEntity<>(new CommonRes(400, "비밀번호가 일치하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        // 액세스 토큰 생성
        String accessToken = userService.createAccessToken(loginUser);

        String refreshToken = userService.createRefreshToken(loginUser);

        return new ResponseEntity<>(new SignupRes(200, "로그인 성공", accessToken, refreshToken), HttpStatus.OK);
    }

    // 토큰 확인용 API
    @GetMapping("/token")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public CommonRes token(@RequestHeader("Authorization") String token) {
        return new CommonRes(200, token);
    }

    @GetMapping("/ping")
    public CommonRes ping() {
        return new CommonRes(200, "pong");
    }


//    유저 프로필이미지 수정은 추후에 구현
//    @PutMapping("/api/v1/user/profile/edit/img/{userNickname}")
//    public void editProfileImage(@PathVariable String userNickname, @RequestBody String profileImage) {
//        User user = userService.findByNickname(userNickname);
//        user.setProfileImage(profileImage);
//    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public CommonRes getMyInfoAndFeeds(@RequestHeader("Authorization") String accessToken) {
        User user = userService.findByToken(accessToken);
        UserWithFeedsDto userWithFeedsDto = userService.findUserAndFeeds(user);
        return new SingleRes<>(200, "유저 정보 조회 성공", userWithFeedsDto);
    }

    @GetMapping("/profile/{userNickname}")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public CommonRes getUserInfoAndFeeds(@RequestHeader("Authorization") String accessToken, @PathVariable String userNickname) {
        User user = userService.findByNickname(userNickname);
        UserWithFeedsDto userWithFeedsDto = userService.findUserAndFeeds(user);
        return new SingleRes<>(200, "유저 정보 조회 성공", userWithFeedsDto);
    }

    @PutMapping("/profile/edit/status")
    public CommonRes editStatusMessage(@RequestHeader("Authorization") String accessToken, @RequestBody UserStatusChangeReq userStatusChangeReq) {
        String userNickname = tokenProvider.getUserNicknameFromToken(accessToken);
        Long userId = userService.findByNickname(userNickname).getId();
        userService.changeUserStatusMessage(userId, userStatusChangeReq.getStatusMessage());
        return new CommonRes(200, "상태메세지 수정 성공");
    }

    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public ResponseEntity<CommonRes> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {

        Long userId = tokenProvider.getUserIdFromRefreshToken(refreshToken);
        User user = userService.findById(userId);
        String accessToken = userService.createAccessToken(user);

        return new ResponseEntity<>(new SingleRes<>(200, "토큰 재발급 성공", accessToken), HttpStatus.OK);
    }

    // 더미 테스트 유저 토큰 발급용 API
    @GetMapping("/vuddy/b305")
    public ResponseEntity<CommonRes> getTest1Token() {
        User testUser = userService.findByNickname("test1");
        String accessToken = userService.createAccessToken(testUser);
        String refreshToken = userService.createRefreshToken(testUser);
        // "accessToken: "accessToken" 이런 형태의 JSON으로 리턴
        return new ResponseEntity<>(new SignupRes(200, "로그인 성공", accessToken, refreshToken), HttpStatus.OK);
    }
}
