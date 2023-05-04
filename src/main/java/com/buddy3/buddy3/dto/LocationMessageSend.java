package com.buddy3.buddy3.dto;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class LocationMessageSend {
    private String nickname;
    private String latitude;
    private String longitude;
    private String time;
}
