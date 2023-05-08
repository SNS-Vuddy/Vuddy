package com.buddy2.buddy2.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisMessageDTO {
    private Long msgId;
    private Long chatId;
    private String content;
    private String nickname;
    private String time;
}
