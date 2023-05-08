package com.edu.ssafy.friend.util;

import java.util.Base64;

public class NicknameUtil {

    public static String decodeNickname(String encodeNickname) {
        return new String(Base64.getDecoder().decode(encodeNickname));
    }
}
