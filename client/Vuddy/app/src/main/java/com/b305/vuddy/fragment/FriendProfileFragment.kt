package com.b305.vuddy.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentFriendProfileBinding
import com.b305.vuddy.model.Feed
import com.b305.vuddy.model.FeedResponse
import com.b305.vuddy.model.Feeds
import com.b305.vuddy.model.FeedsResponse
import com.b305.vuddy.model.UserResponse
import com.b305.vuddy.util.FeedMineAdapter
import com.b305.vuddy.util.RetrofitAPI
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FriendProfileFragment : Fragment() {

    private var nickname: String = ""
    lateinit var binding : FragmentFriendProfileBinding

    private lateinit var feedMineAdapter: FeedMineAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFriendProfileBinding.inflate(layoutInflater, container, false)

        binding.btnAddFriend.setOnClickListener {
            friendAdd()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            nickname = it.getString("nickname", "")
        }

        val call = RetrofitAPI.feedService

        call.FriendfeedGet(nickname).enqueue(object : Callback<FeedsResponse> {
            override fun onResponse(call: Call<FeedsResponse>, response: Response<FeedsResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("GET All", "get successfully. Response: $result")
                    val feedList : ArrayList<Feeds> = result?.FeedList!!
                    // 리사이클러뷰
//                    val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    //격자 레이아웃
                    val layoutManager = GridLayoutManager(context, 3)

                    recyclerView = binding.friendFeedsList
                    recyclerView.layoutManager = layoutManager

                    feedMineAdapter = FeedMineAdapter(feedList)
                    recyclerView.adapter = feedMineAdapter
                } else {
                    Log.d("GET All", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FeedsResponse>, t: Throwable) {
                Log.d("GET All", "get failed.")
            }

        })
        val usercall = RetrofitAPI.userService
        usercall.FriendDataGet(nickname).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("UserData Get", "get successfully. Response: $result")
                    val userNick = binding.friendProfileNickname
                    userNick.text = result?.data?.nickname

                    // 프로필 이미지
                    val profileImageUrl = binding.friendProfileImage
                    val profileImage = result?.data?.profileImage

                    if (profileImage != null) {
                        // 프로필 이미지가 있을 경우 이미지 로드 및 표시
                        Glide.with(requireContext())
                            .load(profileImage)
                            .into(profileImageUrl)
                    } else {
                        // 프로필 이미지가 없을 경우 기본 이미지 표시
                        profileImageUrl.setImageResource(R.drawable.man)
                    }

                } else {
                    Log.d("UserData Get", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("UserData Get", "get failed.")
            }
        })
    }

    fun friendAdd() {
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), "{\"friendNickname\":\"$nickname\"}")

        var friendaddCall = RetrofitAPI.friendService
        friendaddCall.friendAdd(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("친구추가 성공", "get successfully. Response: $result")
                } else {
                    Log.d("친구추가 실패", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("친구추가 실패", "get failed")
            }
        }

        )
    }

}
