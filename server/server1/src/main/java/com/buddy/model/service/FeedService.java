package com.buddy.model.service;

import com.buddy.model.dto.FeedWithTagsDto;
import com.buddy.model.dto.response.SingleFeedRes;
import com.buddy.model.dto.TaggedFriendsDto;
import com.buddy.model.dto.response.UserFeedsRes;
import com.buddy.model.entity.Feed;
import com.buddy.model.entity.TaggedFriends;
import com.buddy.model.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserService userService;

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

    public List<Feed> findAllByToken(String token) {
        Long userId = userService.findByToken(token).getId();
        return feedRepository.findAllByUserId(userId);
    }


    public SingleFeedRes findOneByFeedId(Long feedId) {
        List<FeedWithTagsDto> result = feedRepository.findOneWithTags(feedId);

        if (result.isEmpty()) {
            return null;
        }

        Feed feed = result.get(0).getFeed();

        List<TaggedFriendsDto> taggedFriendsList = result.stream()
                .filter(feedWithTagsResult -> feedWithTagsResult.getTaggedFriend() != null)
                .map(feedWithTagsResult -> {
                    TaggedFriends taggedFriend = feedWithTagsResult.getTaggedFriend();
                    return new TaggedFriendsDto(taggedFriend.getId(), taggedFriend.getNickname());
                })
                .collect(Collectors.toList());

        return SingleFeedRes.builder()
                .feedId(feed.getId())
                .nickname(feed.getNickname())
                .content(feed.getContent())
                .location(feed.getLocation())
                .createdAt(feed.getCreatedAt().toString())
                .updatedAt(feed.getUpdatedAt().toString())
                .taggedFriendsDtoList(taggedFriendsList)
                .build();    }
}
