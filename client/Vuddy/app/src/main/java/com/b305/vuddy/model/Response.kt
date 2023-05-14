package com.b305.vuddy.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(

    @SerializedName("status")
    val status: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("accessToken")
    val accessToken: String?,

    @SerializedName("refreshToken")
    val refreshToken: String?,
)

data class FriendsResponse(
    @SerializedName("data")
    val friendList: ArrayList<FriendProfile>?,
)

data class FeedsResponse(
    @SerializedName("data")
    val FeedList: ArrayList<Feeds>?,
)
data class SearchResponse(
    @SerializedName("data")
    val data: SearchData?,
)

data class SearchData(
    @SerializedName("friends")
    val friends: List<FriendProfile>?,

    @SerializedName("noFriends")
    val noFriends: List<FriendProfile>?
)

data class AlarmResponse(
    @SerializedName("data")
    val AlarmList: ArrayList<Alarm>
)
