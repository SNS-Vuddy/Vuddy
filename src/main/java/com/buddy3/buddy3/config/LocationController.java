package com.buddy3.buddy3.config;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {

    @GetMapping("opened/health")
    public String health() {
        return "hello world";
    }
}
