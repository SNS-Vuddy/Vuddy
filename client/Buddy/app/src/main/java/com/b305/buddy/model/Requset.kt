package com.b305.buddy.model

data class SignupRequest(
    val nickname: String?,
    val password: String?,
    val profileImage: String?,
    val statusMessage: String?
)
