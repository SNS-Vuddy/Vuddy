package com.edu.ssafy.user.model.dto.response;

import com.edu.ssafy.user.model.dto.BriefFeedIngoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileWithFeedsRes {

    private String nickname;
    private String profileImage;
    private String statusMessage;
    private List<BriefFeedIngoDto> feeds;
    private boolean canISeeFeeds;
    @JsonProperty("isFriend")
    private String isFriend;

}
