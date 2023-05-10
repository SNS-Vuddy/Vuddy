package com.b305.vuddy.model

data class FeedDetail(
    val status: Int,
    val message: String,
    val data: FeedData
)
// getter 메서드 생략

data class FeedData(
    val feedId: Int,
    val nickname: String,
    val content: String,
    val location: String,
    val createdAt: String,
    val updatedAt: String,
    val taggedFriends: List<String>,
    val isLiked: Boolean
)

