package com.edu.ssafy.comment.util;

import java.util.Base64;

public class NicknameUtil {

    public static String decodeNickname(String encodeNickname) {
        return new String(Base64.getDecoder().decode(encodeNickname));
    }
}
