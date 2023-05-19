package com.b305.vuddy.model

import com.b305.vuddy.util.BASE_PROFILE_IMG_URL

data class AuthRequest(
    val nickname: String?,
    val password: String?,
    val profileImgUrl: String = BASE_PROFILE_IMG_URL
)
