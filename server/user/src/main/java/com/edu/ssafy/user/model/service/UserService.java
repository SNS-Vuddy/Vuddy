package com.edu.ssafy.user.model.service;

import com.edu.ssafy.user.model.dto.BriefFeedIngoDto;
import com.edu.ssafy.user.model.dto.UserAlarmDto;
import com.edu.ssafy.user.model.dto.UserWithFriendDto;
import com.edu.ssafy.user.model.dto.response.UserFeedsSummaryRes;
import com.edu.ssafy.user.model.dto.response.UserProfileWithFeedsRes;
import com.edu.ssafy.user.model.entity.User;
import com.edu.ssafy.user.model.entity.UserLocation;
import com.edu.ssafy.user.model.entity.enums.FeedPrivacy;
import com.edu.ssafy.user.model.repository.FeedRepository;
import com.edu.ssafy.user.model.repository.UserLocationRepository;
import com.edu.ssafy.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final FeedRepository feedRepository;
    private final UserLocationRepository userLocationRepository;

    public User findById(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public List<User> findAllByNicknameIn(List<String> nicknames) {
        return userRepository.findAllByNicknameIn(nicknames);
    }

    public UserFeedsSummaryRes findUserAndFeeds(User user, boolean hasNewAlarm) {
        List<BriefFeedIngoDto> briefFeedIngoDtoList = feedRepository.findAllBriefInfoByUserId(user.getId());
        return UserFeedsSummaryRes.builder()
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .statusMessage(user.getStatusMessage())
                .has_new_alarm(hasNewAlarm)
                .canISeeFeeds(user.getFeedPrivacy() == FeedPrivacy.PUBLIC)
                .feeds(briefFeedIngoDtoList)
                .build();
    }

    public UserProfileWithFeedsRes findUsersWithFriendStatus(String myNickname, String userNickname) {

        List<User> users = findAllByNicknameIn(List.of(myNickname, userNickname));

        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getNickname, Function.identity()));
        User myUser = userMap.get(myNickname);
        User targetUser = userMap.get(userNickname);

        String friendStatus = userRepository.existsByMyUserNicknameAndTargetUserNickname(myUser, targetUser);

        UserWithFriendDto userWithFriendDto = new UserWithFriendDto(myUser, targetUser, friendStatus);

        List<BriefFeedIngoDto> briefFeedIngoDtoList = feedRepository.findAllBriefInfoByUserId(targetUser.getId());

        boolean canISeeFeeds = targetUser.getFeedPrivacy() == FeedPrivacy.PUBLIC || friendStatus.equals("Yes");

        return UserProfileWithFeedsRes.builder()
                .nickname(userWithFriendDto.getTargetUser().getNickname())
                .profileImage(userWithFriendDto.getTargetUser().getProfileImage())
                .statusMessage(userWithFriendDto.getTargetUser().getStatusMessage())
                .feeds(canISeeFeeds ? briefFeedIngoDtoList : new ArrayList<>())
                .canISeeFeeds(canISeeFeeds)
                .isFriend(friendStatus)
                .build();
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Transactional
    public void changeUserStatusMessage(Long userID, String statusMessage) {
        User user = userRepository.findById(userID).orElse(null);
        Objects.requireNonNull(user).updateStatusMessage(statusMessage);
    }

    public UserAlarmDto findByIdAndAlarm(String nickname) {
        return userRepository.findUserAndAlarm(nickname);
    }

    @Transactional
    public void changeUserProfileImage(String userNickname, String imgUrl) {
        User user = userRepository.findByNickname(userNickname);
        user.changeProfileImage(imgUrl);

    }

    @Transactional
    public void changeUserPrivacy(String userNickname) {
        User user = userRepository.findByNickname(userNickname);
        user.changePrivacy();
    }

    @Transactional
    public void saveHomeAddress(String userNickname, String homeAddress) {
        User user = userRepository.findByNickname(userNickname);
        UserLocation userLocation = userLocationRepository.findById(user.getId())
                .orElseGet(UserLocation::new);
        userLocation.setHomeAddress(user.getId(), homeAddress);
        userLocationRepository.save(userLocation);
    }

    @Transactional
    public void saveOfficeAddress(String userNickname, String officeAddress) {
        User user = userRepository.findByNickname(userNickname);
        UserLocation userLocation = userLocationRepository.findById(user.getId())
                .orElseGet(UserLocation::new);
        userLocation.setOfficeAddress(user.getId(), officeAddress);
        userLocationRepository.save(userLocation);
    }

}
