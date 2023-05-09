package com.b305.vuddy.service

import com.b305.vuddy.model.AuthRequest
import com.b305.vuddy.model.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    
    @Headers("Content-Type: application/json")
    @POST("/user/signup")
    fun signup(@Body authRequest: AuthRequest): Call<AuthResponse>
    
    @Headers("Content-Type: application/json")
    @POST("/user/login")
    fun login(@Body authRequest: AuthRequest): Call<AuthResponse>
}
