package com.buddy.feed.controller;

import com.buddy.feed.service.FeedService;
import com.buddy.feed.dto.*;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@Slf4j // log 사용하기 위한 어노테이션
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
//@Api(tags = {"피드 API"}) // Swagger에서 보이는 controller 이름
public class FeedController {

    @Autowired
    private FeedService feedService;

//    @ApiOperation(value = "") // Swagger에서 보이는 메서드 이름
    @GetMapping("/search/{nicknameStr}")
    public ResponseEntity<FeedSearchRes> getFeedSearchNickname(@PathVariable("nicknameStr") String nicknameStr) {
        System.out.println(nicknameStr);
        FeedSearchRes feedSearchRes = new FeedSearchRes(feedService.getUserStartWith(nicknameStr));
        return ResponseEntity.ok(feedSearchRes);
    }

//    @ApiOperation(value = "회원가입")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createFeed(@Valid @RequestPart(value = "userInfo") InsertUserReq insertUserReq, @RequestPart(value = "profileImg", required = false) MultipartFile multipartFile) throws Exception, DuplicateException, NullPointerException {

        log.debug("회원가입 정보 = {} ", insertUserReq.toString());

        feedService.createFeed(insertUserReq, multipartFile);
        SuccessRes successRes = SuccessRes.builder().message(SUCCESS).build();

        return ResponseEntity.ok().build();
    }
}