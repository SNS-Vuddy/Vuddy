package com.b305.vuddy.model

data class User(
    var nickname: String? = null, var password: String? = null
)
data class UserResponse(
    val status: Int,
    val message: String,
    val data: UserData
)

data class UserData(
    val nickname: String,
    val profileImage: String?,
    val statusMessage: String?,
    val hasNewAlarm: Boolean,
    val feeds: List<Feed>
)

data class Feed(
    val id: Int,
    val imageUrl: String?
)
