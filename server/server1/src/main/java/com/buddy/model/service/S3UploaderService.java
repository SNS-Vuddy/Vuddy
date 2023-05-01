package com.buddy.model.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class S3UploaderService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }


    private String upload(File uploadFile, String dirName) {
        String originalFileName = uploadFile.getName();
        String cleanedFileName = removeSpecialCharactersAndTranslateToEnglish(originalFileName);
        String randomFileName = UUID.randomUUID().toString() + "_" + cleanedFileName;
        String fileName = dirName + "/" + randomFileName;
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    // 한글, 특수문자 등을 제거하고 영어로 변환
    private String removeSpecialCharactersAndTranslateToEnglish(String fileName) {
        String normalized = Normalizer.normalize(fileName, Normalizer.Form.NFD);
        String asciiOnly = normalized.replaceAll("[^\\p{ASCII}]", "");
        String withoutSpecialChars = asciiOnly.replaceAll("[^A-Za-z0-9_.]+", "");
        return withoutSpecialChars;
    }

    private String putS3(File uploadFile, String fileName) {
//        amazonS3Client.putObject(
//                new PutObjectRequest(bucket, fileName, uploadFile)
//                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
//        );

        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                // .withCannedAcl(CannedAccessControlList.PublicRead) // 주석 처리 또는 제거
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("file is deleted");
        }else {
            log.info("file delete fail");
        }
    }

    private Optional<File> convert(MultipartFile file) throws  IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

}
