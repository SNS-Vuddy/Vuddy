package com.edu.ssafy.feed.model.dto.response;

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
    private String createdAt;
    private String updatedAt;
    @JsonProperty("isLiked")
    private boolean isLiked;
    private List<String> taggedFriends;
    private Long likesCount;
    private Long commentsCount;
}
