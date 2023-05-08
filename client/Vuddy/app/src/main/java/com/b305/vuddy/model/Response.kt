package com.b305.vuddy.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    
    @SerializedName("status")
    val status: String?,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("accessToken")
    val accessToken: String?,
    
    @SerializedName("refreshToken")
    val refreshToken: String?,
)
