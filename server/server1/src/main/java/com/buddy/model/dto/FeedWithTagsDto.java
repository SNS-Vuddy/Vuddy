package com.buddy.model.dto;

import com.buddy.model.entity.Feed;
import com.buddy.model.entity.TaggedFriends;
import lombok.Data;

@Data
public class FeedWithTagsDto {
    private Feed feed;
    private TaggedFriends taggedFriend;

    public FeedWithTagsDto(Feed feed, TaggedFriends taggedFriend) {
        this.feed = feed;
        this.taggedFriend = taggedFriend;
    }

}