package com.b305.vuddy.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b305.vuddy.util.RetrofitAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDataViewModel : ViewModel() {

    private val _userData = MutableLiveData<UserResponse?>()
    val userData: LiveData<UserResponse?>
        get() = _userData

    fun loadUserData() {
        val call = RetrofitAPI.userService
        call.userDataGet().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val userresult = response.body()
                    Log.d("UserData Get", "get successfully. Response: $userresult")
                    _userData.value = userresult

                } else {
                    Log.d("UserData Get", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("UserData Get", "get failed.")
            }
        })

    }
}
