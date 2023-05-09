package com.edu.ssafy.friend.controller;

import com.edu.ssafy.friend.model.dto.AllFriendDto;
import com.edu.ssafy.friend.model.dto.common.CommonRes;
import com.edu.ssafy.friend.model.dto.common.ListRes;
import com.edu.ssafy.friend.model.dto.common.SingleRes;
import com.edu.ssafy.friend.model.dto.request.AddFriendReq;
import com.edu.ssafy.friend.model.dto.response.FriendAndNoFriendRes;
import com.edu.ssafy.friend.model.entity.User;
import com.edu.ssafy.friend.model.entity.enums.UserFriendStatus;
import com.edu.ssafy.friend.model.service.FriendService;
import com.edu.ssafy.friend.model.service.UserService;
import com.edu.ssafy.friend.util.NicknameUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    private class UserPair {
        private User requester;
        private User receiver;

        public UserPair(User requester, User receiver) {
            this.requester = requester;
            this.receiver = receiver;
        }

        public User getRequester() {
            return requester;
        }

        public User getReceiver() {
            return receiver;
        }
    }

    private UserPair getRequesterAndReceiver(String encodedNickname, String friendNickname) throws Exception {
        List<User> users = userService.findAllByNicknameIn(List.of(friendNickname, NicknameUtil.decodeNickname(encodedNickname)));
        User requester = users.get(0).getNickname().equals(friendNickname) ? users.get(1) : users.get(0);
        User receiver = users.get(0).getNickname().equals(friendNickname) ? users.get(0) : users.get(1);
        return new UserPair(requester, receiver);
    }

    @PostMapping("/add")
    public ResponseEntity<CommonRes> addFriend(@RequestHeader("x_nickname") String encodedNickname, @RequestBody AddFriendReq req) {
        try {
            UserPair userPair = getRequesterAndReceiver(encodedNickname, req.getFriendNickname());
            User requester = userPair.getRequester();
            User receiver = userPair.getReceiver();
            friendService.requestAddFriend(requester, receiver);
            return ResponseEntity.ok(new CommonRes(200, "친구 추가 요청 성공"));
        } catch (IndexOutOfBoundsException e) {
            return new ResponseEntity<>(new CommonRes(400, "닉네임 정보에 문제가 있습니다."), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonRes(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/accept")
    public ResponseEntity<CommonRes> acceptFriend(@RequestHeader("x_nickname") String encodedNickname, @RequestBody AddFriendReq req) {
        try {
            UserPair userPair = getRequesterAndReceiver(encodedNickname, req.getFriendNickname());
            User requester = userPair.getReceiver();
            User receiver = userPair.getRequester();
            friendService.UpdateFriendStatus(requester, receiver, UserFriendStatus.ACCEPTED);
            return ResponseEntity.ok(new CommonRes(200, "친구 추가 요청 수락 성공"));
        } catch (IndexOutOfBoundsException e) {
            return new ResponseEntity<>(new CommonRes(400, "닉네임 정보에 문제가 있습니다."), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonRes(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // 친구 추가 요청 거절
    @PostMapping("/deny")
    public ResponseEntity<CommonRes> denyFriend(@RequestHeader("x_nickname") String encodedNickname, @RequestBody AddFriendReq req) {
        try {
            UserPair userPair = getRequesterAndReceiver(encodedNickname, req.getFriendNickname());
            User requester = userPair.getReceiver();
            User receiver = userPair.getRequester();
            friendService.UpdateFriendStatus(requester, receiver, UserFriendStatus.DENIED);
            return ResponseEntity.ok(new CommonRes(200, "친구 추가 요청 거절 성공"));
        } catch (IndexOutOfBoundsException e) {
            return new ResponseEntity<>(new CommonRes(400, "닉네임 정보에 문제가 있습니다."), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonRes(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // 친구 삭제
    @PostMapping("/delete")
    public ResponseEntity<CommonRes> deleteFriend(@RequestHeader("x_nickname") String encodedNickname, @RequestBody AddFriendReq req) {
        try {
            UserPair userPair = getRequesterAndReceiver(encodedNickname, req.getFriendNickname());
            User requester = userPair.getReceiver();
            User receiver = userPair.getRequester();
            friendService.deleteFriend(requester, receiver);
            return ResponseEntity.ok(new CommonRes(200, "친구 삭제 성공"));
        } catch (IndexOutOfBoundsException e) {
            return new ResponseEntity<>(new CommonRes(400, "닉네임 정보에 문제가 있습니다."), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonRes(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    //     내 친구 전체 조회
    @GetMapping("/all")
    public ResponseEntity<CommonRes> getAllFriend(@RequestHeader("x_nickname") String encodedNickname) {
        String nickname = NicknameUtil.decodeNickname(encodedNickname);
        User user = userService.findByNickname(nickname);
        List<AllFriendDto> friends = friendService.findAllFriend(user);
        return ResponseEntity.ok(new ListRes<>(200, "친구 전체 조회 성공", friends));
    }

    // 친구, 유저 검색
    @GetMapping("/search")
    public ResponseEntity<CommonRes> searchFriend(@RequestHeader("x_nickname") String encodedNickname, @RequestParam("nickname") String nickname) {

        String myNickname = NicknameUtil.decodeNickname(encodedNickname);

        FriendAndNoFriendRes friendAndNoFriendDto = friendService.searchFriend(myNickname, nickname);

        return ResponseEntity.ok(new SingleRes<>(200, "친구 검색 성공", friendAndNoFriendDto));
    }
}
