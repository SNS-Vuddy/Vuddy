package com.buddy.model.repository.custom;

import com.buddy.model.dto.FeedWithTagsDto;
import com.buddy.model.dto.FeedWithTagsListDto;
import com.buddy.model.entity.Feed;

import java.util.List;
import java.util.Optional;

public interface FeedRepositoryCustom {

    List<FeedWithTagsDto> findOneWithTags(Long id);

    Optional<FeedWithTagsListDto> findFeedWithTagsListById(Long id);

}
