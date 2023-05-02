package com.edu.ssafy.auth.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRoll implements GrantedAuthority {
    NORMAL_USER, KAKAO_USER, ADMIN;
    @Override
    public String getAuthority() {
        return name(); // 현재 열거형의 이름을 반환하도록 변경
    }
}
