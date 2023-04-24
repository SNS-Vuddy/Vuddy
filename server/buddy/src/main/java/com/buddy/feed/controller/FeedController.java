package com.buddy.feed.controller;

import com.buddy.feed.service.FeedService;
import com.buddy.feed.dto.*;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




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
}