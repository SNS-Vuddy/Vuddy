package com.edu.ssafy.feed.model.repository.custom;

import com.edu.ssafy.feed.model.dto.CommentDto;
import com.edu.ssafy.feed.model.entity.Feed;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentDto> findAllByFeed(Feed feed);
}
