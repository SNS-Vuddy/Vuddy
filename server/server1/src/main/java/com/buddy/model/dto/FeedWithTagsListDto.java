package com.buddy.model.dto;

import com.buddy.model.entity.Feed;
import com.buddy.model.entity.TaggedFriends;
import lombok.Data;

import java.util.List;

@Data
public class FeedWithTagsListDto {
    private Feed feed;
    private List<TaggedFriends> taggedFriends;

    public FeedWithTagsListDto(Feed feed, List<TaggedFriends> taggedFriends) {
        this.feed = feed;
        this.taggedFriends = taggedFriends;
    }
}