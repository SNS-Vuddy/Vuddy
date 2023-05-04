package com.edu.ssafy.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class UserApplication {

    @GetMapping
    public String hello(@RequestHeader("x-forwarded-for-nickname") String nickname, @RequestHeader Map<String, String> map) {
        map.forEach((key, value) -> System.out.println(key + " : " + value));
        return nickname;
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
