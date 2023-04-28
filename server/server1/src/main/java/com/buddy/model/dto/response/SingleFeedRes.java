package com.buddy.model.dto.response;

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
    private List<String> taggedFriendsList;
}
