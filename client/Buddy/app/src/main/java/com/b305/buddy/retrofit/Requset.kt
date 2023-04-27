package com.b305.buddy.retrofit

data class SignupRequest(
    val nickname: String?,
    val password: String?,
    val profileImage: String?,
    val statusMessage: String?
)
