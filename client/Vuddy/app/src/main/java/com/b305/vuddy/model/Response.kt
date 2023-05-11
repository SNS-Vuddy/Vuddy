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
<<<<<<< HEAD
    val FeedList: ArrayList<Feeds>?,)
=======
    val FeedList: ArrayList<Feeds>?,
)

>>>>>>> 7e4f989f626a33e991f65d605051bce5049ba4c0
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
