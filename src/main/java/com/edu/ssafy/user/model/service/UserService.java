package com.edu.ssafy.user.model.service;

import com.edu.ssafy.user.model.dto.BriefFeedIngoDto;
import com.edu.ssafy.user.model.dto.response.UserFeedsSummaryRes;
import com.edu.ssafy.user.model.dto.response.UserProfileWithFeedsRes;
import com.edu.ssafy.user.model.entity.User;
import com.edu.ssafy.user.model.repository.FeedRepository;
import com.edu.ssafy.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    public User findById(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public UserFeedsSummaryRes findUserAndFeeds(User user) {
        List<BriefFeedIngoDto> briefFeedIngoDtoList = feedRepository.findAllBriefInfoByUserId(user.getId());
        return new UserFeedsSummaryRes(user.getNickname(), user.getProfileImage(), user.getStatusMessage(), briefFeedIngoDtoList);
    }

    public UserProfileWithFeedsRes findUsersWithFriendStatus(String myNickname, String userNickname) {

        List<User> users = findAllByNicknameIn(List.of(myNickname, userNickname));

        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getNickname, Function.identity()));
        User myUser = userMap.get(myNickname);
        User targetUser = userMap.get(userNickname);

        String friendStatus = userRepository.existsByMyUserNicknameAndTargetUserNickname(myNickname, userNickname);

        UserFriendshipStatusDto userFriendshipStatusDto = new UserFriendshipStatusDto(myUser, targetUser, friendStatus);

        List<BriefFeedIngoDto> briefFeedIngoDtoList = feedRepository.findAllBriefInfoByUserId(myUser.getId());

        return UserProfileWithFeedsRes.builder()
                .nickname(userFriendshipStatusDto.getMyUser().getNickname())
                .profileImage(userFriendshipStatusDto.getMyUser().getProfileImage())
                .statusMessage(userFriendshipStatusDto.getMyUser().getStatusMessage())
                .feeds(briefFeedIngoDtoList)
                .canISeeFeeds(true)
                .isFriend(friendStatus)
                .build();
    }
}
