package com.b305.vuddy.model

data class FeedResponse(
    val status: Int,
    val message: String,
    val data: FeedData
)
// getter 메서드 생략

data class FeedData(
    val feedId: Int,
    val nickname: String,
    val profileImg: String,
    val title: String,
    val content: String,
    val location: String,
    val mainImg: String,
    val images: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val likesCount: Int,
    val commentsCount: Int,
    val comments: List<Comment>,
    val isLiked: Boolean
)

data class Comment(
    val profileImg: String,
    val nickname: String,
    val content: String,
    val createdAt: String
)

data class Feeds(
    val feedId : Int,
    val imgUrl : String,
    val location : String,
)
data class Alarm(
    val nickname: String,
    val profileImage : String,
)
