package com.buddy.model.dto;

import com.buddy.model.entity.Feed;
import com.buddy.model.entity.FeedLikes;
import com.buddy.model.entity.TaggedFriends;
import lombok.Data;

@Data
public class FeedWithTagsDto {
    private Feed feed;
    private TaggedFriends taggedFriend;
    private FeedLikes feedLikes;

    public FeedWithTagsDto(Feed feed, TaggedFriends taggedFriend, FeedLikes feedLikes) {
        this.feed = feed;
        this.taggedFriend = taggedFriend;
        this.feedLikes = feedLikes;
    }
}