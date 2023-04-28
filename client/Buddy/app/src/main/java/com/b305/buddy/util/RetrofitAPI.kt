package com.b305.buddy.util

import com.b305.buddy.service.SignupService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitAPI {
    // Todo: Change BASE_URL to your server's IP address
    //private const val BASE_URL = "http://localhost:8080"
    private const val BASE_URL = "http://10.0.2.2:8080"
    
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
    
    val signupService: SignupService by lazy {
        retrofit.create(SignupService::class.java)
    }
}