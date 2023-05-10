package com.b305.vuddy.service

import com.b305.vuddy.model.FeedDetail
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FeedService {
    @Multipart
    @POST("/feed/opened/image")
    fun feedImg(
        @Part images: List<MultipartBody.Part>
    ): Call<ResponseBody>

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

    @GET("/feed/1")
    fun feedGet(
        @Path("id") id: Int
    ): Response<FeedDetail>

}