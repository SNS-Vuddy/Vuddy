package com.edu.ssafy.auth.model.service;

import com.edu.ssafy.auth.jwt.TokenProvider;
import com.edu.ssafy.auth.model.entity.User;
import com.edu.ssafy.auth.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public Long join(User user) {
        validateDuplicateUser(user); // 중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        if (userRepository.findByNickname(user.getNickname()) != null) {
            throw new IllegalStateException("이 회원은 이미 존재합니다.");
        }
    }

    public String createAccessToken(User user) {
        String nickname = user.getNickname();
        String encodedNickname = Base64.getEncoder().encodeToString(nickname.getBytes(StandardCharsets.UTF_8));

        // 액세스 토큰 생성을 위한 인증 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(encodedNickname, user.getPassword(), Collections.singletonList(user.getUserRoll()));
        return tokenProvider.createAccessToken(authentication);
    }

    public String createRefreshToken(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getNickname(), user.getPassword(), Collections.singletonList(user.getUserRoll()));
        return tokenProvider.createRefreshToken(user);
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

}
