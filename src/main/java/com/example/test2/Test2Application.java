package com.example.test2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Test2Application {

    @RequestMapping("/test/health")
    String hello(){
        return "Hello World";
    }

    public static void main(String[] args) {
        SpringApplication.run(Test2Application.class, args);
    }

}
