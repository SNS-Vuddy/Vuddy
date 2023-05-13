package com.edu.ssafy.feed.model.service;

import com.edu.ssafy.feed.model.dto.AllFeedInfoDto;
import com.edu.ssafy.feed.model.dto.CommentDto;
import com.edu.ssafy.feed.model.dto.FeedWithTagsListDto;
import com.edu.ssafy.feed.model.dto.request.FeedEditReq;
import com.edu.ssafy.feed.model.dto.response.FriendsFeedsRes;
import com.edu.ssafy.feed.model.dto.response.SingleFeedRes;
import com.edu.ssafy.feed.model.dto.response.UserFeedsRes;
import com.edu.ssafy.feed.model.entity.*;
import com.edu.ssafy.feed.model.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final CommentRepository commentRepository;
    private final FeedPictureRepository feedPictureRepository;

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
            UserFeedsRes userFeedsRes = new UserFeedsRes(feed.getId(), feed.getMainImg(), feed.getLocation());
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

        List<CommentDto> commentDtoList = commentRepository.findAllByFeed(feed);

        List<String> images = feedPictureRepository.findAllByFeedId(feed.getId()).stream()
                .map(FeedPictures::getImgUrl)
                .collect(Collectors.toList());

        return SingleFeedRes.builder()
                .feedId(feed.getId())
                .nickname(feed.getNickname())
                .title(feed.getTitle())
                .content(feed.getContent())
                .location(feed.getLocation())
                .mainImg(feed.getMainImg())
                .images(images)
                .createdAt(feed.getCreatedAt().toString())
                .updatedAt(feed.getUpdatedAt().toString())
                .isLiked(isLiked)
                .isMine(Objects.equals(feed.getNickname(), nickname))
                .likesCount((long) likesCount.size())
                .commentsCount((long) commentDtoList.size())
                .comments(commentDtoList)
                .build();
    }

    @Transactional
    public void editFeed(Long feedId, FeedEditReq req) {
        Feed feed = feedRepository.findFeedWithTagsListById(feedId)
                .map(FeedWithTagsListDto::getFeed)
                .orElseThrow(() -> new IllegalArgumentException("해당 피드가 존재하지 않습니다."));

        feed.updateContentAndLocation(req.getTitle(), req.getContent(), req.getLocation());

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

        return findFeedLike.map(this::unlikeFeed)
                .orElseGet(() -> likeFeed(feed, user));
    }

    private String unlikeFeed(FeedLikes feedLike) {
        feedLikesRepository.delete(feedLike);
        return "좋아요 취소 성공";
    }

    private String likeFeed(Feed feed, User user) {
        FeedLikes feedLikes = FeedLikes.createLike(feed, user);
        feedLikesRepository.save(feedLikes);
        return "좋아요 성공";
    }

    public List<FriendsFeedsRes> findAllByUserIdIn(List<Long> friends) {
        return feedRepository.findAllByUserIdIn(friends);
    }

    @Transactional
    public void dummyFeeds() {
        for (int i = 10; i < 13; i++) {
            Feed feed = Feed.builder()
                    .user(userRepository.findById((long) i).orElse(null))
                    .nickname("nickname" + i)
                    .title("title" + i)
                    .content("content" + i)
                    .location("location" + i)
                    .mainImg("mainImg" + i)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            feedRepository.save(feed);
        }
    }
}
