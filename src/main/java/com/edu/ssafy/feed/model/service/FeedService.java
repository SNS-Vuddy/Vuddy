package com.edu.ssafy.feed.model.service;

import com.edu.ssafy.feed.model.dto.AllFeedInfoDto;
import com.edu.ssafy.feed.model.dto.CommentDto;
import com.edu.ssafy.feed.model.dto.FeedWithTagsListDto;
import com.edu.ssafy.feed.model.dto.request.FeedEditReq;
import com.edu.ssafy.feed.model.dto.response.SingleFeedRes;
import com.edu.ssafy.feed.model.dto.response.UserFeedsRes;
import com.edu.ssafy.feed.model.entity.*;
import com.edu.ssafy.feed.model.repository.FeedLikesRepository;
import com.edu.ssafy.feed.model.repository.FeedRepository;
import com.edu.ssafy.feed.model.repository.TaggedFriendsRepository;
import com.edu.ssafy.feed.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;
    private final TaggedFriendsRepository taggedFriendsRepository;
    private final UserRepository userRepository;
    private final FeedLikesRepository feedLikesRepository;

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
        List<AllFeedInfoDto> result = feedRepository.findOneWithTags(feedId);
        if (result.isEmpty()) {
            return null;
        }

        Feed feed = result.get(0).getFeed();

        List<CommentDto> comments = result.stream()
                .map(AllFeedInfoDto::getComments)
                .filter(Objects::nonNull)
                .map(c -> new CommentDto(c.getNickname(), c.getContent(), c.getCreatedAt()))
                .collect(Collectors.toList());

        boolean isLiked = result.stream()
                .map(AllFeedInfoDto::getFeedLikes)
                .filter(Objects::nonNull)
                .map(FeedLikes::getUser)
                .filter(Objects::nonNull)
                .anyMatch(user -> Objects.equals(user.getNickname(), nickname));

        Set<Long> likesCount = result.stream()
                .map(AllFeedInfoDto::getFeedLikes)
                .filter(Objects::nonNull)
                .map(FeedLikes::getId)
                .collect(Collectors.toSet());

        Set<Long> commentsCount = result.stream()
                .map(AllFeedInfoDto::getComments)
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
                .likesCount((long) likesCount.size())
                .commentsCount((long) commentsCount.size())
                .comments(comments)
                .build();
    }

    @Transactional
    public void editFeed(Long feedId, FeedEditReq req) {
        Feed feed = feedRepository.findFeedWithTagsListById(feedId)
                .map(FeedWithTagsListDto::getFeed)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드가 존재하지 않습니다."));

        feed.updateContentAndLocation(req.getContent(), req.getLocation());

        taggedFriendsRepository.deleteByFeedId(feedId);

        // 이미지는 구현 예정
        // imagesRepository.deleteByFeedId(feedId);

        List<String> userNicknames = req.getTags();

        Map<String, User> userMap = userRepository.findAllByNicknameIn(userNicknames).stream()
                .collect(Collectors.toMap(User::getNickname, user -> user));

        List<TaggedFriends> newTaggedFriends = userNicknames.stream()
                .map(tag -> {
//                    User taggedUser = userRepository.findByNickname(tag);
                    User taggedUser = userMap.get(tag);
                    return req.toTagEntity(feed, taggedUser);
                })
                .collect(Collectors.toList());
        taggedFriendsRepository.saveAll(newTaggedFriends);
    }

    @Transactional
    public String likeFeed(Long feedId, String nickname) {

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드가 존재하지 않습니다."));

        User user = userRepository.findByNickname(nickname);

        Optional<FeedLikes> findFeedLike = feedLikesRepository.findByFeedAndUser(feed, user);

        if (findFeedLike.isPresent()) {
            feedLikesRepository.delete(findFeedLike.get());
            return "좋아요 취소 성공";
        } else {
            FeedLikes feedLikes = FeedLikes.createLike(feed, user);
            feedLikesRepository.save(feedLikes);
            return "좋아요 성공";
        }
    }
}
