package com.buddy.controller;

import com.buddy.model.dto.common.CommonRes;
import com.buddy.model.dto.request.RequestAddFriendReq;
import com.buddy.model.entity.User;
import com.buddy.model.entity.enums.UserFriendStatus;
import com.buddy.model.service.FriendService;
import com.buddy.model.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    private final UserService userService;
    private final FriendService friendService;


    //친구 추가 요청
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public ResponseEntity<CommonRes> addFriend(@RequestHeader("Authorization") String token, @RequestBody RequestAddFriendReq req) {
        User requester, receiver;
        try {
            List<User> users = userService.findAllByNicknameIn(List.of(req.getFriendNickname(), userService.findUserNicknameByToken(token)));
            receiver = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(0) : users.get(1);
            requester = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(1) : users.get(0);

        } catch (Exception e) {
            return new ResponseEntity<>(new CommonRes(400, "유저의 닉네임 정보에 문제가 있습니다."), HttpStatus.BAD_REQUEST);
        }

        friendService.requestAddFriend(requester, receiver);
        return ResponseEntity.ok(new CommonRes(200, "친구 추가 요청 성공"));

    }

    // 친구 추가 요청 수락
    @PostMapping("/accept")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public ResponseEntity<CommonRes> acceptFriend(@RequestHeader("Authorization") String token, @RequestBody RequestAddFriendReq req) {
        User requester, receiver;

        try {
            List<User> users = userService.findAllByNicknameIn(List.of(req.getFriendNickname(), userService.findUserNicknameByToken(token)));

            requester = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(0) : users.get(1);
            receiver = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(1) : users.get(0);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonRes(400, "유저의 닉네임 정보에 문제가 있습니다."), HttpStatus.BAD_REQUEST);
        }

        friendService.UpdateFriendStatus(requester, receiver, UserFriendStatus.ACCEPTED);

        return ResponseEntity.ok(new CommonRes(200, "친구 추가 요청 수락 성공"));
    }

    // 친구 추가 요청 거절
    @PostMapping("/deny")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public ResponseEntity<CommonRes> denyFriend(@RequestHeader("Authorization") String token, @RequestBody RequestAddFriendReq req) {
        User requester, receiver;
        try {
            List<User> users = userService.findAllByNicknameIn(List.of(req.getFriendNickname(), userService.findUserNicknameByToken(token)));

            requester = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(0) : users.get(1);
            receiver = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(1) : users.get(0);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonRes(400, "유저의 닉네임 정보에 문제가 있습니다."), HttpStatus.BAD_REQUEST);
        }

        friendService.UpdateFriendStatus(requester, receiver, UserFriendStatus.DENIED);
        return ResponseEntity.ok(new CommonRes(200, "친구 추가 요청 거절 성공"));
    }

    // 친구 삭제
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public ResponseEntity<CommonRes> deleteFriend(@RequestHeader("Authorization") String token, @RequestBody RequestAddFriendReq req) {
        User requester, receiver;

        try {
            List<User> users = userService.findAllByNicknameIn(List.of(req.getFriendNickname(), userService.findUserNicknameByToken(token)));

            requester = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(0) : users.get(1);
            receiver = users.get(0).getNickname().equals(req.getFriendNickname()) ? users.get(1) : users.get(0);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonRes(400, "유저의 닉네임 정보에 문제가 있습니다."), HttpStatus.BAD_REQUEST);
        }

        friendService.deleteFriend(requester, receiver);
        return ResponseEntity.ok(new CommonRes(200, "친구 삭제 성공"));
    }

    // 내 친구 전체 조회
//    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
//    public ResponseEntity<CommonRes> getAllFriend(@RequestHeader("Authorization") String token) {
//        String userNickname = userService.findUserNicknameByToken(token);
//        List<User> friends = friendService.findAllFriend(user);
//        return ResponseEntity.ok(new CommonRes(200, "친구 전체 조회 성공", friends));
//    }

}
