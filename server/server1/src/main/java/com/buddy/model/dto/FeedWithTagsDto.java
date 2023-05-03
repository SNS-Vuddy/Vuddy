package com.buddy.model.dto;

import com.buddy.model.entity.Comments;
import com.buddy.model.entity.Feed;
import com.buddy.model.entity.FeedLikes;
import com.buddy.model.entity.TaggedFriends;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedWithTagsDto {
    private Feed feed;
    private TaggedFriends taggedFriend;
    private FeedLikes feedLikes;
    private Comments comments;

}