package com.b305.vuddy.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.databinding.FragmentAlarmBinding
import com.b305.vuddy.model.Alarm
import com.b305.vuddy.model.AlarmResponse
import com.b305.vuddy.viewmodel.AlarmViewModel
import com.b305.vuddy.util.adapter.AlarmAdapter
import com.b305.vuddy.service.RetrofitAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class AlarmFragment : Fragment(), AlarmAdapter.AlarmCallback {

    lateinit var binding : FragmentAlarmBinding

    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: AlarmViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment

        binding = FragmentAlarmBinding.inflate(layoutInflater, container, false)
        recyclerView = binding.friendAlarmList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        alarmAdapter = AlarmAdapter(arrayListOf(), this@AlarmFragment)
        recyclerView.adapter = alarmAdapter

        binding.commentBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

//        viewModel.loadAlarmList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        viewModel.loadAlarmList() // loadAlarmList() 호출 위치 변경

        viewModel.alarmList.observe(viewLifecycleOwner) { alarm ->
            alarmAdapter.setData(ArrayList(alarm))
        }

        val call = RetrofitAPI.friendService
        call.friendRequest().enqueue(
            object : Callback<AlarmResponse> {
                override fun onResponse(call: Call<AlarmResponse>, response: Response<AlarmResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("친구목록 불러오기 성공", "get successfully. Response: $result")
                        // 알림 RecyclerView
                        val alarmList : ArrayList<Alarm> = result?.AlarmList!!

//                        recyclerView = binding.friendAlarmList
//                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//                        val adapter = AlarmAdapter(alarmList, this@AlarmFragment)
//                        recyclerView.adapter = adapter
//
//                        viewModel.alarmList.observe(viewLifecycleOwner) { alarm ->
//                            alarmAdapter.setData(ArrayList(alarm))
//                        }
                        alarmAdapter.setData(alarmList)
                        viewModel.loadAlarmList()

                    } else {
                        Log.d("친구목록 불러오기 실패", "get failed. Response: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<AlarmResponse>, t: Throwable) {
                    Log.e("친구목록 불러오기 실패", "get failed.", t)
                }
            })
    }

    override fun onFriendAccept(friendNickname: String) {
        val requestBody = "{\"friendNickname\":\"$friendNickname\"}"
            .toRequestBody("application/json".toMediaTypeOrNull())

        val alarmcall = RetrofitAPI.friendService
        alarmcall.friendAccept(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("친구요청 수락 성공", "get successfully. Response: $result")
                    viewModel.loadAlarmList()
                } else {
                    Log.d("친구요청 수락 실패", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("친구요청 수락 실패", "get failed.")
            }
        })
    }

    override fun onFriendDeny(friendNickname: String) {
        val requestBody = "{\"friendNickname\":\"$friendNickname\"}"
            .toRequestBody("application/json".toMediaTypeOrNull())

        val alarmcall = RetrofitAPI.friendService
        alarmcall.friendRefuse(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("친구요청 거절 성공", "get successfully. Response: $result")
                    viewModel.loadAlarmList()
                } else {
                    Log.d("친구요청 거절 실패", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("친구요청 거절 실패", "get failed.}")
            }
        })
    }

}
