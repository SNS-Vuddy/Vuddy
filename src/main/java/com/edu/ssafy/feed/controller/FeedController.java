package com.edu.ssafy.feed.controller;

import com.edu.ssafy.feed.model.dto.common.CommonRes;
import com.edu.ssafy.feed.model.dto.common.ListRes;
import com.edu.ssafy.feed.model.dto.common.SingleRes;
import com.edu.ssafy.feed.model.dto.request.FeedEditReq;
import com.edu.ssafy.feed.model.dto.request.FeedWriteReq;
import com.edu.ssafy.feed.model.dto.response.SingleFeedRes;
import com.edu.ssafy.feed.model.dto.response.UserFeedsRes;
import com.edu.ssafy.feed.model.entity.Feed;
import com.edu.ssafy.feed.model.entity.User;
import com.edu.ssafy.feed.model.service.FeedService;
import com.edu.ssafy.feed.model.service.TaggedFriendsService;
import com.edu.ssafy.feed.model.service.UserService;
import com.edu.ssafy.feed.util.NicknameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedController {

    private final UserService userService;
    private final FeedService feedService;
    private final TaggedFriendsService taggedFriendsService;

    @GetMapping("/test")
    public String test(@RequestHeader("x-forwarded-for-nickname") String encodedNickname) {

        return NicknameUtil.decodeNickname(encodedNickname);
    }

    //피드 작성
    @PostMapping("/write")
    public CommonRes writeFeed(@RequestHeader("x-forwarded-for-nickname") String encodedNickname, @RequestBody FeedWriteReq req) {
        String nickname = NicknameUtil.decodeNickname(encodedNickname);
        // 유저 정보 가져오기
        User user = userService.findByNickname(nickname);

//         FeedWriteReq를 Feed 엔티티로 변환
        Feed feed = req.toFeedEntity(user);

//         변환된 엔티티를 저장
        feedService.saveFeed(feed);

        taggedFriendsService.saveAllTaggedFriends(req, feed);

        return new CommonRes(201, "피드 작성 성공");
    }

    // 내 피드 전체 조회
    @GetMapping("/feeds/mine")
    public ResponseEntity<ListRes<UserFeedsRes>> getMyAllFeed(@RequestHeader("x-forwarded-for-nickname") String encodedNickname) {
        String nickname = NicknameUtil.decodeNickname(encodedNickname);
        User user = userService.findByNickname(nickname);

        List<Feed> allFeeds = feedService.findAllByUserId(user.getId());

        List<UserFeedsRes> allUserFeedsRes = feedService.changeAllFeedsToDto(allFeeds);
        return new ResponseEntity<>(new ListRes<>(200, "피드 조회 성공", allUserFeedsRes), HttpStatus.OK);
    }

    // 특정 유저 피드 전체 조회
    @GetMapping("/feeds/nickname/{targetNickname}")
    public ResponseEntity<ListRes<UserFeedsRes>> getUserAllFeed(@RequestHeader("x-forwarded-for-nickname") String encodedNickname, @PathVariable String targetNickname) {
        User targetUser = userService.findByNickname(targetNickname);
        List<Feed> allFeeds = feedService.findAllByUserId(targetUser.getId());

        List<UserFeedsRes> allUserFeedsRes = feedService.changeAllFeedsToDto(allFeeds);
        ListRes<UserFeedsRes> listRes = new ListRes<>(200, "피드 조회 성공", allUserFeedsRes);
        return new ResponseEntity<>(listRes, HttpStatus.OK);
    }

    // 피드 상세 조회
    @GetMapping("/{feedId}")
    public SingleRes<SingleFeedRes> getFeedDetail(@RequestHeader("x-forwarded-for-nickname") String encodedNickname, @PathVariable Long feedId) {
        String nickname = NicknameUtil.decodeNickname(encodedNickname);
        SingleFeedRes oneByFeedId = feedService.findOneByFeedId(feedId, nickname);
        return new SingleRes<>(200, "피드 상세 조회 성공", oneByFeedId);
    }

    // 피드 수정
    @PutMapping("/edit/{feedId}")
    public CommonRes editFeed(@PathVariable Long feedId, @RequestHeader("x-forwarded-for-nickname") String encodedNickname, @RequestBody FeedEditReq req) {
        feedService.editFeed(feedId, req);
        return new CommonRes(200, "피드 수정 성공");
    }

    // 피드 좋아요 / 좋아요 취소
    @PostMapping("/like/{feedId}")
    public ResponseEntity<?> likeFeed(@PathVariable Long feedId, @RequestHeader("x-forwarded-for-nickname") String encodedNickname) {
        String nickname = NicknameUtil.decodeNickname(encodedNickname);
        String msg = feedService.likeFeed(feedId, nickname);
        return new ResponseEntity<>(new CommonRes(200, msg), HttpStatus.OK);
    }
}
