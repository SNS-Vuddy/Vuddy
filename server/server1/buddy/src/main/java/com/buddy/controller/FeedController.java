package com.buddy.controller;

import com.buddy.model.dto.common.CommonRes;
import com.buddy.model.dto.common.ListRes;
import com.buddy.model.dto.request.FeedWriteReq;
import com.buddy.model.dto.response.UserFeedsRes;
import com.buddy.model.entity.Feed;
import com.buddy.model.entity.TaggedFriends;
import com.buddy.model.entity.User;
import com.buddy.model.service.FeedService;
import com.buddy.model.service.TaggedFriendsService;
import com.buddy.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final UserService userService;
    private final TaggedFriendsService taggedFriendsService;

    //피드 작성
    @PostMapping("/api/v1/feed/write/{userId}")
    public CommonRes writeFeed(@PathVariable("userId") Long userId, @RequestBody FeedWriteReq req) {

        // 유저 정보 가져오기
        User user = userService.findByUserId(userId);

        // FeedWriteReq를 Feed 엔티티로 변환
        Feed feed = req.toFeedEntity(user);

        // 변환된 엔티티를 저장
        feedService.saveFeed(feed);

//         tags리스트에 담긴 닉네임들을 통해 유저를 찾아서 feed에 저장
        for (String nickname : req.getTags()) {
            User tagUser = userService.findByNickname(nickname);
            TaggedFriends taggedFriends = req.toTagEntity(feed, tagUser);
            taggedFriendsService.saveTaggedFriends(taggedFriends);
        }

        return new CommonRes(201, "피드 작성 성공");
    }

    //특정 유저의 피드 조회
    @GetMapping("/api/v1/feed/{userId}")
    public ResponseEntity<ListRes<UserFeedsRes>> getUserFeeds(@PathVariable("userId") Long userId) {
        List<Feed> allFeeds = feedService.findAllByUserId(userId);
        List<UserFeedsRes> allUserFeedsRes = feedService.changeAllFeedsToDto(allFeeds);
        log.info("allUserFeedsRes = {}", allUserFeedsRes);
        ListRes<UserFeedsRes> listRes = new ListRes<>(200, "피드 조회 성공", allUserFeedsRes);
        return new ResponseEntity<>(listRes, HttpStatus.OK);
    }

    // 피드 상세 조회
//    @GetMapping("/api/v1/feed/{feedId}")
//    @ResponseStatus(HttpStatus.ACCEPTED)


}
