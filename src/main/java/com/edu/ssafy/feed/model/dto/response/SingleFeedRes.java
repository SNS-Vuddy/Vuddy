package com.edu.ssafy.feed.model.dto.response;

import com.edu.ssafy.feed.model.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SingleFeedRes {
    private Long feedId;
    private String nickname;
    private String content;
    private String location;
    private String mainImg;
    private List<String> images;
    private String createdAt;
    private String updatedAt;
    @JsonProperty("isLiked")
    private boolean isLiked;
    private Long likesCount;
    private Long commentsCount;
    private List<CommentDto> comments;
}
