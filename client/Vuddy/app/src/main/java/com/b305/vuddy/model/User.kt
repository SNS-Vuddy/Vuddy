package com.b305.vuddy.model

import android.os.Parcelable

data class User(
    var nickname: String? = null,
    var password: String? = null,
    var profileImgUrl: String? = null,
    var statusImgUrl: String? = null,
)

data class UserResponse(
    val status: Int,
    val message: String,
    val data: UserData
)

data class UserData(
    val nickname: String,
    var profileImage: String?,
    val statusMessage: String?,
    val hasNewAlarm: Boolean,
    var canISeeFeeds : Boolean,
    val feeds: List<Feed>
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
    var profileImage: String?,
    val statusMessage: String?,
    val hasNewAlarm: Boolean,
    var canISeeFeeds : Boolean,
    val feeds: List<Feed>,
    val isFriend : String,
)



