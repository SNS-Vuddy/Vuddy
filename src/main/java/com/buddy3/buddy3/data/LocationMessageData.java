package com.buddy3.buddy3.data;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

@Data
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationMessageData {
    private String nickname;
    private String latitude;
    private String longitude;
    private String time;
    private String status;
}
