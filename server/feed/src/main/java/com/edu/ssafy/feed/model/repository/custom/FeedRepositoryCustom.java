package com.edu.ssafy.feed.model.repository.custom;

import com.edu.ssafy.feed.model.dto.AllFeedInfoDto;
import com.edu.ssafy.feed.model.dto.FeedWithTagsListDto;
import com.edu.ssafy.feed.model.dto.response.FriendsFeedsRes;

import java.util.List;
import java.util.Optional;

public interface FeedRepositoryCustom {
    List<AllFeedInfoDto> findOneWithTags(Long feedId);

    Optional<FeedWithTagsListDto> findFeedWithTagsListById(Long id);

    List<FriendsFeedsRes> findAllByUserIdIn(List<Long> friends);
}
