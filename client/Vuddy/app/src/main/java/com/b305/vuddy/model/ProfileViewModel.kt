package com.b305.vuddy.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b305.vuddy.util.RetrofitAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    private val _profile = MutableLiveData<UserResponse?>()
    val profile: MutableLiveData<UserResponse?>
        get() = _profile

    fun loadProfile() {
        val call = RetrofitAPI.userService
        call.UserDataGet().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userresult = response.body()
                    Log.d("GET Detail", "get successfully. Response: $userresult")
                    _profile.value = userresult
                } else {
                    Log.d("GET Detail", "get failed. Response: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}
