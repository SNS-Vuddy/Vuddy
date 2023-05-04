package com.edu.ssafy.user.model.repository.custom;

import com.edu.ssafy.user.model.dto.BriefFeedIngoDto;

import java.util.List;

public interface FeedRepositoryCustom {

    List<BriefFeedIngoDto> findAllBriefInfoByUserId(Long userId);

}
