package com.buddy.model.service;

import com.buddy.jwt.TokenProvider;
import com.buddy.model.dto.UserWithFeedsDto;
import com.buddy.model.dto.response.BriefFeedIngoDto;
import com.buddy.model.entity.User;
import com.buddy.model.repository.FeedRepository;
import com.buddy.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final FeedRepository feedRepository;

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

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public User findByToken(String token) {
        return userRepository.findByNickname(tokenProvider.getUserNicknameFromToken(token));
    }

    public String findUserNicknameByToken(String token) {
        return tokenProvider.getUserNicknameFromToken(token);
    }

    public List<User> findAllByNicknameIn(List<String> nicknames) {
        return userRepository.findAllByNicknameIn(nicknames);
    }

    @Transactional
    public void changeUserStatusMessage(Long userID, String statusMessage) {
        User user = userRepository.findById(userID).orElse(null);
        Objects.requireNonNull(user).updateStatusMessage(statusMessage);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public String createAccessToken(User user) {
        // 액세스 토큰 생성을 위한 인증 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getNickname(), user.getPassword(), Collections.singletonList(user.getUserRoll()));
        return tokenProvider.createAccessToken(authentication);
    }

    public String createRefreshToken(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getNickname(), user.getPassword(), Collections.singletonList(user.getUserRoll()));
        return tokenProvider.createRefreshToken(user);
    }


    public UserWithFeedsDto findUserAndFeeds(User user) {
        List<BriefFeedIngoDto> briefFeedIngoDtoList = feedRepository.findAllBriefInfoByUserId(user.getId());
        return new UserWithFeedsDto(user.getNickname(), user.getProfileImage(), user.getStatusMessage(), briefFeedIngoDtoList);
    }
}
