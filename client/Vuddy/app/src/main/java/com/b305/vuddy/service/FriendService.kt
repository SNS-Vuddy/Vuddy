package com.b305.vuddy.service

import com.b305.vuddy.model.FriendsResponse
import com.b305.vuddy.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FriendService {
    
    @Headers("Content-Type: application/json")
    @GET("/friend/all")
    fun getFriendList(): Call<FriendsResponse>

    @Headers("Content-Type: application/json")
    @GET("/friend/search")
    fun search(@Query("nickname") nickName: String): Call<SearchResponse>
}
