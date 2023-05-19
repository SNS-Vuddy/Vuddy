package com.buddy3.buddy3.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationMessageReceive {
    private String nickname;
    private String latitude;
    private String longitude;
    private String localDateTime;
    private String accessToken;
    private String status;
    private String imgUrl;
}
