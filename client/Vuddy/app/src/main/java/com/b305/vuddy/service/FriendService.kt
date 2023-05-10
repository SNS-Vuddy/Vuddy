package com.b305.vuddy.service

import com.b305.vuddy.model.FriendsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface FriendService {
    
    @Headers("Content-Type: application/json")
    @GET("/friend/all")
    fun getFriendList(): Call<FriendsResponse>

}
