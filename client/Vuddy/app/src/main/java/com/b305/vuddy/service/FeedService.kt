package com.b305.vuddy.service

import com.b305.vuddy.model.FeedResponse
import com.b305.vuddy.model.FeedsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FeedService {
//    @Multipart
//    @POST("/feed/opened/image")
//    fun feedImg(
//        @Part images: List<MultipartBody.Part>
//    ): Call<ResponseBody>

//    @Multipart
//    @POST("/feed/write")
//    @Headers("Content-Type: multipart/form-data")
//    fun feedWrite(
//        @Part("title") title: String,
//        @Part("content") content: String,
//        @Part("location") location: String,
//        @Part("tags") tags: List<String>,
//        @Part images: List<MultipartBody.Part>
//    ) : Call<ResponseBody>

    @Multipart
    @POST("/feed/write")
//    @Headers("Content-Type: multipart/form-data")
    fun feedWrite(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("location") location: RequestBody,
        @Part("tags") tags: ArrayList<RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Call<ResponseBody>

//    @GET("/feed/1")
//    fun feedDetailGet(): Observable<FeedResponse>
    @POST("/comment/write/{feedId}")
    @Headers("Content-Type: application/json")
    fun commentWrite(
        @Path("feedId") feedId : Int,
        @Body comment : RequestBody,
    ): Call<ResponseBody>

    @POST("/feed/like/{feedId}")
    fun feedLike(
        @Path("feedId") feedId : Int,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("/feed/{feedId}")
    fun feedDetailGet(
        @Path("feedId") feedId: Int
    ): Call<FeedResponse>

    @Headers("Content-Type: application/json")
    @GET("/feed/feeds/mine")
    fun feedMineGet() : Call<FeedsResponse>

//    @GET("/feed/feeds/nickname/test1")
//    fun feedUserGet() : Call<FeedsResponse>

    @Headers("Content-Type: application/json")
    @GET("/feed/feeds/nickname/{nickname}")
    fun friendfeedGet(
        @Path("nickname") nickname: String,
    ): Call<FeedsResponse>
}
