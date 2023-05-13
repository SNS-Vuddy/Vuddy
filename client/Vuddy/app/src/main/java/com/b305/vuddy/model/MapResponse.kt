package com.b305.vuddy.model

import com.google.gson.annotations.SerializedName


data class MapFeedResponse(
    @SerializedName("data")
    val mapFeedList: ArrayList<MapFeed>?
)

data class MapFeed(
    val feedId: Int,
    val nickname:String,
    val content: String,
    val imgUrl: String,
    val location: String
)
