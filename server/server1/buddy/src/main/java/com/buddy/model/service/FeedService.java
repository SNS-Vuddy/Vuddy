package com.buddy.model.service;

import com.buddy.model.dto.response.UserFeedsRes;
import com.buddy.model.entity.Feed;
import com.buddy.model.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;

    @Transactional
    public void saveFeed(Feed feed) {
        feedRepository.save(feed);
    }

    public List<Feed> findAllByUserId(Long userId) {
        return feedRepository.findAllByUserId(userId);
    }

    public List<UserFeedsRes> changeAllFeedsToDto(List<Feed> allFeeds) {

        List<UserFeedsRes> userFeedsResList = new ArrayList<>();

        for (Feed feed : allFeeds) {
            UserFeedsRes userFeedsRes = new UserFeedsRes(feed.getId(), feed.getContent());
            userFeedsResList.add(userFeedsRes);
        }

        return userFeedsResList;

    }
}
