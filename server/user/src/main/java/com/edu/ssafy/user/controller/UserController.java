package com.edu.ssafy.user.controller;

import com.edu.ssafy.user.annotation.LogExecutionTime;
import com.edu.ssafy.user.model.dto.UserAlarmDto;
import com.edu.ssafy.user.model.dto.common.CommonRes;
import com.edu.ssafy.user.model.dto.common.SingleRes;
import com.edu.ssafy.user.model.dto.request.UserHomeSaveReq;
import com.edu.ssafy.user.model.dto.request.UserOfficeSaveReq;
import com.edu.ssafy.user.model.dto.request.UserStatusChangeReq;
import com.edu.ssafy.user.model.dto.response.UserFeedsSummaryRes;
import com.edu.ssafy.user.model.dto.response.UserProfileWithFeedsRes;
import com.edu.ssafy.user.model.service.S3UploaderService;
import com.edu.ssafy.user.model.service.UserService;
import com.edu.ssafy.user.util.NicknameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final S3UploaderService s3UploaderService;

    @GetMapping("/opened/health")
    public String health() {
        return "hello world";
    }

//     내 프로필, 피드들 조회
    @LogExecutionTime
    @GetMapping("/profile")
    public CommonRes getMyInfoAndFeeds(@RequestHeader("x_nickname") String encodedNickname) {
        String nickname = NicknameUtil.decodeNickname(encodedNickname);
        UserAlarmDto userAlarmDto = userService.findByIdAndAlarm(nickname);
        UserFeedsSummaryRes myUserWithFeedsDto = userService.findUserAndFeeds(userAlarmDto.getUser(), userAlarmDto.isHasNewAlarm());
        return new SingleRes<>(200, "유저 정보 조회 성공", myUserWithFeedsDto);
    }

    // 특정 유저 프로필, 피드들 조회
    @LogExecutionTime
    @GetMapping("/profile/{targetUserNickname}")
    public CommonRes getUserInfoAndFeeds(@RequestHeader("x_nickname") String encodedNickname, @PathVariable String targetUserNickname) {
        String userNickname = NicknameUtil.decodeNickname(encodedNickname);

        UserProfileWithFeedsRes userProfileWithFeedsRes = userService.findUsersWithFriendStatus(userNickname, targetUserNickname);

        return new SingleRes<>(200, "유저 정보 조회 성공", userProfileWithFeedsRes);
    }

    // 상태메세지 수정
    @LogExecutionTime
    @PutMapping("/profile/edit/status")
    public CommonRes editStatusMessage(@RequestHeader("x_nickname") String encodedNickname, @RequestBody UserStatusChangeReq userStatusChangeReq) {
        String userNickname = NicknameUtil.decodeNickname(encodedNickname);
        Long userId = userService.findByNickname(userNickname).getId();
        userService.changeUserStatusMessage(userId, userStatusChangeReq.getStatusMessage());
        return new CommonRes(200, "상태메세지 수정 성공");
    }

    // 프로필 이미지 수정
    @LogExecutionTime
    @PutMapping("/profile/edit/image")
    public CommonRes editProfileImage(@RequestHeader("x_nickname") String encodedNickname, @RequestPart(value = "images") List<MultipartFile> images) {
        String userNickname = NicknameUtil.decodeNickname(encodedNickname);

        ExecutorService executorService = Executors.newFixedThreadPool(5); // 스레드 풀 생성
        List<Future<String>> futures = new ArrayList<>();

        for (MultipartFile image : images) {
            // 이미지 업로드 작업을 스레드 풀에 제출
            Future<String> future = executorService.submit(() -> s3UploaderService.upload(image, "images"));
            futures.add(future);
        }

        List<String> storedFileNames = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                storedFileNames.add(future.get()); // 업로드된 이미지의 URL을 가져옴
            } catch (Exception e) {
                throw new RuntimeException("이미지 업로드 중 에러 발생", e);
            }
        }

        executorService.shutdown(); // 스레드 풀 종료

        userService.changeUserProfileImage(userNickname, storedFileNames.get(0));
        return new CommonRes(200, "프로필 이미지 수정 성공");
    }

    // 피드 공개 여부 수정 api
    @LogExecutionTime
    @PostMapping("/profile/edit/privacy")
    public CommonRes editPrivacy(@RequestHeader("x_nickname") String encodedNickname) {
        String userNickname = NicknameUtil.decodeNickname(encodedNickname);
        userService.changeUserPrivacy(userNickname);
        return new CommonRes(200, "피드 공개 여부 수정 성공");
    }

    // 유저 집 주소 저장 api
    @LogExecutionTime
    @PostMapping("/address/home")
    public CommonRes saveHomeAddress(@RequestHeader("x_nickname") String encodedNickname, @RequestBody UserHomeSaveReq req) {
        String userNickname = NicknameUtil.decodeNickname(encodedNickname);
        userService.saveHomeAddress(userNickname, req.getHomeAddress());
        return new CommonRes(200, "유저 집 주소 저장 성공");
    }

    // 유저 회사 주소 저장 api
    @LogExecutionTime
    @PostMapping("/address/office")
    public CommonRes saveOfficeAddress(@RequestHeader("x_nickname") String encodedNickname, @RequestBody UserOfficeSaveReq req) {
        String userNickname = NicknameUtil.decodeNickname(encodedNickname);
        userService.saveOfficeAddress(userNickname, req.getOfficeAddress());
        return new CommonRes(200, "유저 회사 주소 저장 성공");
    }

}
