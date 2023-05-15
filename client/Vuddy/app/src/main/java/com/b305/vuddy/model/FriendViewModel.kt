package com.b305.vuddy.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b305.vuddy.util.RetrofitAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendViewModel : ViewModel() {
    private val _friendData = MutableLiveData<FriendResponse?>()
    val friendData: MutableLiveData<FriendResponse?>
        get() = _friendData

    fun loadFriendData(nickname: String) {
        val call = RetrofitAPI.userService
        call.FriendDataGet(nickname).enqueue(object : Callback<FriendResponse> {
            override fun onResponse(call: Call<FriendResponse>, response: Response<FriendResponse>) {
                if (response.isSuccessful) {
                    val friendresult = response.body()
                    Log.d("GET Detail", "get successfully. Response: $friendresult")
                    _friendData.value = friendresult

                } else {
                    Log.d("GET Detail", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FriendResponse>, t: Throwable) {
                Log.d("GET Detail", "get failed. Response: ")
            }
        })
    }
}
