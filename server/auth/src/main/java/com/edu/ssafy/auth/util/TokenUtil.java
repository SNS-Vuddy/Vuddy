package com.edu.ssafy.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.security.Key;

public class TokenUtil {

    private static Key key;

    public static String getUserNicknameFromToken(String token) {

        String assertToken = token.substring(7);

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(assertToken)
                .getBody();

        return claims.getSubject();
    }
}
