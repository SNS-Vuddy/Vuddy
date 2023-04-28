package com.buddy.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindUserRes {
    private String nickname;
    private String profileImage;
    private String statusMessage;

}
