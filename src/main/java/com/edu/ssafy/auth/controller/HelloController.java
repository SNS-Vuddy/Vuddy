package com.edu.ssafy.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/auth/health")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/auth/opened/health")
    public String hello2() {
        return "Hello World!";
    }

}
