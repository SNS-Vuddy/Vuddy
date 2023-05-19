package com.b305.vuddy.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b305.vuddy.model.Alarm
import com.b305.vuddy.model.AlarmResponse
import com.b305.vuddy.service.RetrofitAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmViewModel : ViewModel() {

    private val _alarmList = MutableLiveData<List<Alarm>>()
    val alarmList: LiveData<List<Alarm>> = _alarmList

    fun loadAlarmList() {
        // API 호출하여 데이터 가져오는 코드
        val call = RetrofitAPI.friendService
        call.friendRequest().enqueue(
            object : Callback<AlarmResponse> {
                override fun onResponse(call: Call<AlarmResponse>, response: Response<AlarmResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("친구목록 불러오기 성공", "get successfully. Response: $result")
                        // 알림 RecyclerView
                        val alarmList : List<Alarm> = result?.AlarmList ?: emptyList()
                        _alarmList.setValue(alarmList)
                    } else {
                        Log.d("친구목록 불러오기 실패", "get failed. Response: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<AlarmResponse>, t: Throwable) {
                    Log.e("친구목록 불러오기 실패", "get failed.", t)
                }
            })
    }
}

