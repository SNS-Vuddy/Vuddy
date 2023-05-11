package com.buddy2.buddy2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    @GetMapping("opened/health")
    public String health(){
        return "hello world";
    }
}
