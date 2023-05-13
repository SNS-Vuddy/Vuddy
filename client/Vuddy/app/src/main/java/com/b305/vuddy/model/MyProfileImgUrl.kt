package com.b305.vuddy.model

import com.google.gson.annotations.SerializedName

data class MyProfileImgUrl(
    @SerializedName("status")
    val status: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("profileImage")
    val myProfileImgUrl: String
)
