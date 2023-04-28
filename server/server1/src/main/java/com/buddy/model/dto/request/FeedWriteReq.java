package com.buddy.model.dto.request;

import com.buddy.model.entity.Feed;
import com.buddy.model.entity.TaggedFriends;
import com.buddy.model.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedWriteReq {

    private String content;
    private List<String> images; // 이미지를 문자열 목록으로 받기 위해 리스트로 선언
    private String location;
    private List<String> tags; // 태그를 여러 개 받기 위해 리스트로 선언

    public Feed toFeedEntity(User user) {
        return Feed.builder()
                .user(user)
                .nickname(user.getNickname())
                .content(content)
                .location(location)
                // 태그와 이미지는 추후에 추가
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public TaggedFriends toTagEntity(Feed feed, User user) {
        return TaggedFriends.builder()
                .feed(feed)
                .user(user)
                .nickname(user.getNickname())
                .build();
    }
}
