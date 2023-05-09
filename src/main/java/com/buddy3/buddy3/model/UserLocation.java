package com.buddy3.buddy3.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Setter
@Getter
@Document(collection = "user_location")
@NoArgsConstructor
public class UserLocation {
    @Id
    private String id;
    private String nickname;
    private String latitude;
    private String longitude;
    private String time;
}
