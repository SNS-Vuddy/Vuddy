package com.b305.vuddy.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b305.vuddy.util.RetrofitAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedDetailViewModel : ViewModel() {
    private val _feedDetail = MutableLiveData<FeedResponse?>()
    val feedDetail: MutableLiveData<FeedResponse?>
        get() = _feedDetail

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    fun loadFeedDetail(feedId: Int) {
        val call = RetrofitAPI.feedService
        call.feedDetailGet(feedId).enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    val feedresult = response.body()
                    Log.d("GET Detail", "get successfully. Response: $feedresult")
                    _feedDetail.value = feedresult
                    _comments.value = feedDetail.value?.data?.comments
                } else {
                    Log.d("GET Detail", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Log.e("GET Detail", "get failed.", t)
            }
        })
    }
    fun addComment(comment: Comment) {
        val commentList = _comments.value?.toMutableList() ?: mutableListOf()
        commentList.add(comment)
        _comments.value = commentList
    }
}
