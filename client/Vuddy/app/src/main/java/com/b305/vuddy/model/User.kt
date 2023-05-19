package com.b305.vuddy.model

data class User(
    var nickname: String? = null,
    var password: String? = null,
    var profileImgUrl: String? = null,
    var statusImgUrl: String? = null
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
    val canISeeFeeds: Boolean,
    val feeds: ArrayList<Feed>
)

data class Feed(
    val id: Int,
    val imageUrl: String?
)

data class FriendResponse(
    val status: Int,
    val message: String,
    val data: FriendData
)

data class FriendData(
    val nickname: String,
    val profileImage: String?,
    val statusMessage: String?,
    val canISeeFeeds: Boolean,
    val feeds: ArrayList<Feed>,
    val isFriend: String,
)
