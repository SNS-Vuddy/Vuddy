package com.edu.ssafy.feed.feed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Base64;

@SpringBootApplication
public class FeedApplication {

    @GetMapping("/feed/hello")
    public String hello(@RequestHeader("x-forwarded-for-nickname") String nickname) {
        //Base64 로 nickname 디코딩하기
        String userNickname = new String(Base64.getDecoder().decode(nickname));
        
        return userNickname;
    }

    public static void main(String[] args) {
        SpringApplication.run(FeedApplication.class, args);
    }

}
