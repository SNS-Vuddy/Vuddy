package com.b305.vuddy.service

import com.b305.vuddy.model.FeedResponse
import com.b305.vuddy.model.UserData
import com.b305.vuddy.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserService {
    @Headers("Content-Type: application/json")
    @GET("/user/profile")
    fun UserDataGet(): Call<UserResponse>

    @Headers("Content-Type: application/json")
    @GET("/user/profile/{nickname}")
    fun FriendDataGet(
        @Path("nickname") nickname: String,
    ): Call<UserResponse>

}
