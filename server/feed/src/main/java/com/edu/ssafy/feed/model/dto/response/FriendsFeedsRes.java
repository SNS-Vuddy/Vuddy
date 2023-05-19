package com.edu.ssafy.feed.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendsFeedsRes {

    private Long feedId;
    private String imgUrl;
    private String location;

}
