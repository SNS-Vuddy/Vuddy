package com.b305.vuddy.service

import com.b305.vuddy.model.MapFeedResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MapFeedService {

    @Headers("Content-Type: application/json")
    @GET("/feed/feeds/friends")
    fun getAllFriendsFeed(): Call<MapFeedResponse>

    @Headers("Content-Type: application/json")
    @GET("/feed/feeds/nickname/{nickname}")
    fun getOneFriendFeed(@Path("nickname") nickname: String): Call<MapFeedResponse>
}
