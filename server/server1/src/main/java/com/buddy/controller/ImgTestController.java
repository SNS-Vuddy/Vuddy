package com.buddy.controller;

import com.buddy.model.service.ImgTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImgTestController {

    private final ImgTestService imgTestService;

    @PostMapping("/img")
    public String saveImg(@RequestPart(value = "img") MultipartFile img) {

        System.out.println("=================================================================");
        System.out.println("img = " + img.getOriginalFilename());
        System.out.println("=================================================================");

        try {
            String s = imgTestService.saveImg(img);
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
