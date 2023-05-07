package com.edu.ssafy.feed.model.repository.custom;

import com.edu.ssafy.feed.model.dto.FeedWithTagsDto;

import java.util.List;

public interface FeedRepositoryCustom {
    List<FeedWithTagsDto> findOneWithTags(Long feedId);
}
