package com.edu.ssafy.feed.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFeedsRes {

    private Long feedId;
    private String imgUrl;
    private String location;
}
