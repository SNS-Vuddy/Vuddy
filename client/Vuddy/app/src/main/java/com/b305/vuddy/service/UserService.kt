package com.b305.vuddy.service

import com.b305.vuddy.model.FeedResponse
import com.b305.vuddy.model.FriendResponse
import com.b305.vuddy.model.UserData
import com.b305.vuddy.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserService {
    @Headers("Content-Type: application/json")
    @GET("/user/profile")
    fun UserDataGet(): Call<UserResponse>

    @Headers("Content-Type: application/json")
    @GET("/user/profile/{nickname}")
    fun FriendDataGet(
        @Path("nickname") nickname: String,
    ): Call<FriendResponse>

    @Multipart
    @PUT("/user/profile/edit/image")
    fun ProfileImgChange(
        @Part images: List<MultipartBody.Part>
    ): Call<ResponseBody>

}
