package com.buddy3.buddy3.data;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Data
@Setter
public class LocationMessageData {
    private String nickname;
    private String latitude;
    private String longitude;
    private String time;
}
