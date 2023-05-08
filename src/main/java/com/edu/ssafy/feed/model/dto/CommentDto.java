package com.edu.ssafy.feed.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
}
