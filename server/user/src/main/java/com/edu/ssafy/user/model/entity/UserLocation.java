package com.edu.ssafy.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_location")
public class UserLocation {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "home_address")
    private String homeAddress;

    @Column(name = "office_address")
    private String officeAddress;

    public void setHomeAddress(Long userId, String homeAddress) {
        this.userId = userId;
        this.homeAddress = homeAddress;
    }

    public void setOfficeAddress(Long userId, String officeAddress) {
        this.userId = userId;
        this.officeAddress = officeAddress;
    }
}