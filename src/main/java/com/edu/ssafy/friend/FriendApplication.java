package com.edu.ssafy.friend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class FriendApplication {

    @GetMapping("/opened/hello")
    public String hello() {

        return "hello";
    }

    public static void main(String[] args) {
        SpringApplication.run(FriendApplication.class, args);
    }
}
