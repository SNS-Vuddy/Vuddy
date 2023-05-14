package com.b305.vuddy.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.model.FriendProfile
import com.b305.vuddy.model.FriendsResponse
import com.b305.vuddy.util.MapProfileAdapter
import com.b305.vuddy.util.RetrofitAPI
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class FriendListBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var mapProfileAdapter: MapProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_friend_list_bottom_sheet, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_profile)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = RetrofitAPI.friendService
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_profile)

        service.getFriendList().enqueue(/* callback = */ object : retrofit2.Callback<FriendsResponse> {
            override fun onResponse(call: Call<FriendsResponse>, response: Response<FriendsResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show()
                    val result = response.body()
                    val friendList: ArrayList<FriendProfile> = result?.friendList!!
                    val layoutManager = LinearLayoutManager(context)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.setHasFixedSize(true)
                    mapProfileAdapter = MapProfileAdapter(friendList)
                    recyclerView.adapter = mapProfileAdapter

                    recyclerView.post {
                        // 바텀 시트의 높이를 리사이클러 뷰의 높이에 맞게 조절
                        val params = (view.parent as View).layoutParams

                        // 조회한 친구가 3명 이하일 때 일정 높이로 최소 크기 설정
                        val minHeight = resources.getDimensionPixelSize(R.dimen.bottom_sheet_min_height)
                        if (friendList.size <= 3) {
                            params.height = minHeight
                        } else {
                            params.height = recyclerView.height
                        }

                        (view.parent as View).layoutParams = params

                        // 최대 높이를 설정하여 바텀 시트가 화면을 가리지 않도록 함
                        val maxHeight = (resources.displayMetrics.heightPixels * 0.8).toInt()
                        if (params.height > maxHeight) {
                            params.height = maxHeight
                            (view.parent as View).layoutParams = params
                        }
                    }

                } else {
                    val errorMessage = JSONObject(response.errorBody()?.string()!!)
                    Toast.makeText(context, errorMessage.getString("message"), Toast.LENGTH_SHORT).show()
                    Log.d(ContentValues.TAG, "$response")
                }
            }

            override fun onFailure(call: Call<FriendsResponse>, t: Throwable) {
                Toast.makeText(context, "친구목록 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
