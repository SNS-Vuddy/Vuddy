package com.edu.ssafy.feed.feed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FeedApplication {

    @GetMapping("/feed/hello")
    public String hello() {
        
        return "hello";
    }

    public static void main(String[] args) {
        SpringApplication.run(FeedApplication.class, args);
    }

}
