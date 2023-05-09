package com.b305.buddy.service

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FeedService {
    @Multipart
    @POST("/img")
    fun feedImg(
        @Part ("imageFile") imageFile: MultipartBody.Part,
    ) : Call<ResponseBody>
}
