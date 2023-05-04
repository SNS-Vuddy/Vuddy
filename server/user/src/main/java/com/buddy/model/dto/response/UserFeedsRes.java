package com.buddy.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFeedsRes {

    private Long feedId;
    private String content;
    private String mainImg;
}
