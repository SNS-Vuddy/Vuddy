package com.buddy.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImgTestService {

    @Autowired
    private S3UploaderService s3UploaderService;

    @Transactional
    public String saveImg(MultipartFile image) throws IOException {
        if(!image.isEmpty()) {
            String storedFileName = s3UploaderService.upload(image,"images");
            System.out.println("=================================================================");
            System.out.println("storedFileName = " + storedFileName);
            System.out.println("=================================================================");
            return storedFileName;
        }
        return "ㅇㅇㅇ";

    }

}
