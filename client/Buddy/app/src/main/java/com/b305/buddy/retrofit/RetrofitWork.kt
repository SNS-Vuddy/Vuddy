package com.b305.buddy.retrofit

import android.util.Log
import retrofit2.Call
import retrofit2.Response

class RetrofitWork(private val signupRequest: SignupRequest) {
    
    fun work() {
        val service = RetrofitAPI.emgMedService
        service.signup(signupRequest)
            .enqueue(object : retrofit2.Callback<SignupResponse> {
                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("회원가입 성공", "$result")
                    }
                }
                
                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Log.d("회원가입 실패", t.message.toString())
                }
            })
    }
}