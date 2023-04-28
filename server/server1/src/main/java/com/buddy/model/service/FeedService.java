package com.buddy.model.service;

import com.buddy.model.dto.FeedWithTagsDto;
import com.buddy.model.dto.FeedWithTagsListDto;
import com.buddy.model.dto.request.FeedEditReq;
import com.buddy.model.dto.response.SingleFeedRes;
import com.buddy.model.dto.response.UserFeedsRes;
import com.buddy.model.entity.Feed;
import com.buddy.model.entity.TaggedFriends;
import com.buddy.model.entity.User;
import com.buddy.model.repository.FeedRepository;
import com.buddy.model.repository.TaggedFriendsRepository;
import com.buddy.model.repository.UserRepository;
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
    private final TaggedFriendsRepository taggedFriendsRepository;
    private final UserService userService;
    private final UserRepository userRepository;

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

        List<String> taggedFriendsList = result.stream()
                .filter(feedWithTagsResult -> feedWithTagsResult.getTaggedFriend() != null)
                .map(feedWithTagsResult -> {
                    TaggedFriends taggedFriend = feedWithTagsResult.getTaggedFriend();
                    return taggedFriend.getNickname();
                })
                .collect(Collectors.toList());

        return SingleFeedRes.builder()
                .feedId(feed.getId())
                .nickname(feed.getNickname())
                .content(feed.getContent())
                .location(feed.getLocation())
                .createdAt(feed.getCreatedAt().toString())
                .updatedAt(feed.getUpdatedAt().toString())
                .taggedFriendsList(taggedFriendsList)
                .build();
    }

    @Transactional
    public void editFeed(Long feedId, FeedEditReq req) {
        FeedWithTagsListDto feedWithTagsListDto = feedRepository.findFeedWithTagsListById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드가 존재하지 않습니다."));

        Feed feed = feedWithTagsListDto.getFeed();

        feed.updateContentAndLocation(req.getContent(), req.getLocation());

        taggedFriendsRepository.deleteByFeedId(feedId);

        // 이미지는 구현 예정
        // imagesRepository.deleteByFeedId(feedId);

        List<TaggedFriends> newTaggedFriends = req.getTags().stream()
                .map(tag -> {
                    User taggedUser = userRepository.findByNickname(tag);
                    return req.toTagEntity(feed, taggedUser);
                })
                .collect(Collectors.toList());
        taggedFriendsRepository.saveAll(newTaggedFriends);
    }
}
