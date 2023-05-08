package com.edu.ssafy.feed.model.dto;

import com.edu.ssafy.feed.model.entity.Comments;
import com.edu.ssafy.feed.model.entity.Feed;
import com.edu.ssafy.feed.model.entity.FeedLikes;
import com.edu.ssafy.feed.model.entity.TaggedFriends;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllFeedInfoDto {
    private Feed feed;
    private FeedLikes feedLikes;
    private Comments comments;

}