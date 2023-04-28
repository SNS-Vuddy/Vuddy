package com.buddy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloContoller {

    @GetMapping("/auth/health")
    public String hello() {
        return "Hello World!";
    }

}
