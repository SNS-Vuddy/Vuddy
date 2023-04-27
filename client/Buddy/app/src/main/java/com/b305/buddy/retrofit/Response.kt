package com.b305.buddy.retrofit

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    
    @SerializedName("status")
    val status: String?,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("accessToken")
    val accessToken: String?
)