package com.edu.ssafy.feed.model.service;

import com.edu.ssafy.feed.model.dto.FeedWithTagsDto;
import com.edu.ssafy.feed.model.dto.response.SingleFeedRes;
import com.edu.ssafy.feed.model.dto.response.UserFeedsRes;
import com.edu.ssafy.feed.model.entity.Comments;
import com.edu.ssafy.feed.model.entity.Feed;
import com.edu.ssafy.feed.model.entity.FeedLikes;
import com.edu.ssafy.feed.model.entity.TaggedFriends;
import com.edu.ssafy.feed.model.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;

    @Transactional
    public void saveFeed(Feed feed) {
        feedRepository.save(feed);
    }

    public List<Feed> findAllByUserId(Long id) {
        return feedRepository.findAllByUserId(id);
    }

    public List<UserFeedsRes> changeAllFeedsToDto(List<Feed> allFeeds) {

        List<UserFeedsRes> userFeedsResList = new ArrayList<>();

        for (Feed feed : allFeeds) {
            UserFeedsRes userFeedsRes = new UserFeedsRes(feed.getId(), feed.getContent(), feed.getMainImg());
            userFeedsResList.add(userFeedsRes);
        }

        return userFeedsResList;
    }

    public SingleFeedRes findOneByFeedId(Long feedId, String nickname) {
        List<FeedWithTagsDto> result = feedRepository.findOneWithTags(feedId);
        if (result.isEmpty()) {
            return null;
        }

        Feed feed = result.get(0).getFeed();

        List<String> taggedFriendsList = result.stream()
                .map(FeedWithTagsDto::getTaggedFriend)
                .filter(Objects::nonNull)
                .map(TaggedFriends::getNickname)
                .distinct()
                .collect(Collectors.toList());

        boolean isLiked = result.stream()
                .map(FeedWithTagsDto::getFeedLikes)
                .filter(Objects::nonNull)
                .map(FeedLikes::getUser)
                .filter(Objects::nonNull)
                .anyMatch(user -> Objects.equals(user.getNickname(), nickname));

        Set<Long> likesCount = result.stream()
                .map(FeedWithTagsDto::getFeedLikes)
                .filter(Objects::nonNull)
                .map(FeedLikes::getId)
                .collect(Collectors.toSet());

        Set<Long> commentsCount = result.stream()
                .map(FeedWithTagsDto::getComments)
                .filter(Objects::nonNull)
                .map(Comments::getId)
                .collect(Collectors.toSet());

        return SingleFeedRes.builder()
                .feedId(feed.getId())
                .nickname(feed.getNickname())
                .content(feed.getContent())
                .location(feed.getLocation())
                .createdAt(feed.getCreatedAt().toString())
                .updatedAt(feed.getUpdatedAt().toString())
                .isLiked(isLiked)
                .taggedFriends(taggedFriendsList)
                .likesCount((long) likesCount.size())
                .commentsCount((long) commentsCount.size())
                .build();
    }
}
