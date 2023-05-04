package com.edu.ssafy.user.model.service;

import com.edu.ssafy.user.model.dto.BriefFeedIngoDto;
import com.edu.ssafy.user.model.dto.response.UserFeedsSummaryRes;
import com.edu.ssafy.user.model.entity.User;
import com.edu.ssafy.user.model.repository.FeedRepository;
import com.edu.ssafy.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
