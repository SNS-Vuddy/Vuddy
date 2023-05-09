package com.edu.ssafy.auth.controller;

import com.edu.ssafy.auth.jwt.TokenProvider;
import com.edu.ssafy.auth.model.dto.common.CommonRes;
import com.edu.ssafy.auth.model.dto.common.SingleRes;
import com.edu.ssafy.auth.model.dto.request.LoginReq;
import com.edu.ssafy.auth.model.dto.request.SignupReq;
import com.edu.ssafy.auth.model.dto.response.SignupRes;
import com.edu.ssafy.auth.model.entity.User;
import com.edu.ssafy.auth.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

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


    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String token) {
        log.info("validate 시작");
        if (token == null) {
            return new ResponseEntity<>(new CommonRes(400, "토큰이 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        if (!token.startsWith("Bearer ")) {
            return new ResponseEntity<>(new CommonRes(400, "Bearer로 시작하지 않는 토큰입니다."), HttpStatus.BAD_REQUEST);
        }
        String accessToken = token.substring(7);
        if (!tokenProvider.validateToken(accessToken)) {
            return new ResponseEntity<>(new CommonRes(400, "유효하지 않은 토큰입니다."), HttpStatus.BAD_REQUEST);
        }

        // 토큰에서 인코딩된 닉네임 추출
        String encodedNickname = tokenProvider.getUserNicknameFromToken(token);
        log.info("validate 유저 검증 완료");
        
        return ResponseEntity.ok().header("x-nickname", encodedNickname).build();
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

    // 리프레쉬 토큰으로 액세스 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<CommonRes> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {

        Long userId = tokenProvider.getUserIdFromRefreshToken(refreshToken);
        User user = userService.findById(userId);
        String accessToken = userService.createAccessToken(user);

        return new ResponseEntity<>(new SingleRes<>(200, "토큰 재발급 성공", accessToken), HttpStatus.OK);
    }


}
