package com.b305.vuddy.service

import com.b305.vuddy.model.AlarmResponse
import com.b305.vuddy.model.FriendsResponse
import com.b305.vuddy.model.SearchResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendService {
    
    @Headers("Content-Type: application/json")
    @GET("/friend/all")
    fun getFriendList(): Call<FriendsResponse>

    @Headers("Content-Type: application/json")
    @GET("/friend/search")
    fun search(@Query("nickname") nickName: String): Call<SearchResponse>

    @Headers("Content-Type: application/json")
    @POST("/friend/add")
    fun friendAdd(
        @Body friendNickname : RequestBody,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("/friend/request")
    fun friendRequest(): Call<AlarmResponse>

    @Headers("Content-Type: application/json")
    @POST("/friend/accept")
    fun friendAccept(
        @Body friendNickname : RequestBody,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/friend/deny")
    fun friendRefuse(
        @Body friendNickname : RequestBody,
    ): Call<ResponseBody>

}
