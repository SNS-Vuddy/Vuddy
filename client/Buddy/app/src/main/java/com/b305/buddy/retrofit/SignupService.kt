package com.b305.buddy.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SignupService {
    
    @Headers("Content-Type: application/json")
    @POST("/api/v1/user/signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>
}