package com.buddy.model.service;

import com.buddy.jwt.TokenProvider;
import com.buddy.model.dto.MyUserWithFeedsDto;
import com.buddy.model.dto.UserWithFeedsDto;
import com.buddy.model.dto.UserWithFriendDto;
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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public UserWithFeedsDto findUsersWithFriendStatus(String myNickname, String userNickname) {

        List<User> users = findAllByNicknameIn(List.of(myNickname, userNickname));

        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getNickname, Function.identity()));
        User myUser = userMap.get(myNickname);
        User targetUser = userMap.get(userNickname);

        String friendStatus = userRepository.existsByMyUserNicknameAndTargetUserNickname(myNickname, userNickname);

        UserWithFriendDto userWithFriendDto = new UserWithFriendDto(myUser, targetUser, friendStatus);

        List<BriefFeedIngoDto> briefFeedIngoDtoList = feedRepository.findAllBriefInfoByUserId(myUser.getId());

        return UserWithFeedsDto.builder()
                .nickname(userWithFriendDto.getMyUser().getNickname())
                .profileImage(userWithFriendDto.getMyUser().getProfileImage())
                .statusMessage(userWithFriendDto.getMyUser().getStatusMessage())
                .feeds(briefFeedIngoDtoList)
                .canISeeFeeds(true)
                .isFriend(friendStatus)
                .build();
    }


    public MyUserWithFeedsDto findUserAndFeeds(User myUser) {
        List<BriefFeedIngoDto> briefFeedIngoDtoList = feedRepository.findAllBriefInfoByUserId(myUser.getId());

        return new MyUserWithFeedsDto(myUser.getNickname(), myUser.getProfileImage(), myUser.getStatusMessage(), briefFeedIngoDtoList);
    }
}
