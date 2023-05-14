package com.b305.vuddy.model

data class FeedResponse(
    val status: Int,
    val message: String,
    val data: FeedData
)
// getter 메서드 생략

data class FeedData(
    val feedId: Int,
    val title : String,
    val nickname: String,
    val content: String,
    val location: String,
    val mainImg: String,
    val images: List<String>,
    val createdAt: String,
    val updatedAt: String,
    var likesCount: Int,
    val commentsCount: Int,
    var comments: List<Comment>,
    var isLiked: Boolean
)

data class Comment(
    val id: Int,
    val userId: Int,
    val nickname: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String
)

data class Feeds(
    val feedId : Int,
    val imgUrl : String,
    val location : String,
)
//data class AlarmResponse(
//    val status: Int,
//    val message: String,
//    val data : AlarmList,
//)

data class Alarm(
    val nickname: String,
    val profileImage : String,
)
