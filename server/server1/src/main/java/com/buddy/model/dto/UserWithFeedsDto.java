package com.buddy.model.dto;

import com.buddy.model.dto.response.BriefFeedIngoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithFeedsDto {

    private String nickname;
    private String profileImage;
    private String statusMessage;
    private List<BriefFeedIngoDto> briefFeedIngoDtoList;

}
