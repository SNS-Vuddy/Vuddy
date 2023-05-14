package com.b305.vuddy.model

import com.google.gson.annotations.SerializedName


data class MapFeedResponse(
    @SerializedName("data")
    val mapFeedList: ArrayList<MapFeed>?
)

data class MapFeed(

    @SerializedName("feedId")
    val feedId: Int,

    @SerializedName("imgUrl")
    val imgUrl: String,

    @SerializedName("location")
    val location: String
)
