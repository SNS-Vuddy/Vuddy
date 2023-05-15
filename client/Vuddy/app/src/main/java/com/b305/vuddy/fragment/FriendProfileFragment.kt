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
import com.b305.vuddy.model.FriendResponse
import com.b305.vuddy.util.FeedMineAdapter
import com.b305.vuddy.util.RetrofitAPI
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
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
        val usercall = RetrofitAPI.userService
        usercall.friendDataGet(nickname).enqueue(object : Callback<FriendResponse> {
            override fun onResponse(call: Call<FriendResponse>, response: Response<FriendResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()

                    // 피드
                    val feedList : ArrayList<Feed> = result?.data?.feeds as ArrayList<Feed>
                    val layoutManager = GridLayoutManager(context, 3)
                    recyclerView = binding.friendFeedsList
                    recyclerView.layoutManager = layoutManager

                    feedMineAdapter = FeedMineAdapter(feedList)
                    recyclerView.adapter = feedMineAdapter

                    // 닉네임
                    val userNick = binding.friendProfileNickname
                    userNick.text = result.data.nickname

                    // 프로필 이미지
                    val profileImageUrl = binding.friendProfileImage
                    val profileImage = result.data.profileImage

                    if (profileImage != null) {
                        // 프로필 이미지가 있을 경우 이미지 로드 및 표시
                        Glide.with(requireContext())
                            .load(profileImage)
                            .into(profileImageUrl)
                    } else {
                        // 프로필 이미지가 없을 경우 기본 이미지 표시
                        profileImageUrl.setImageResource(R.drawable.man)
                    }

                    // 버튼 설정
                    val isFriend = result.data.isFriend
                    when (isFriend) {
                        "Yes" -> {
                            binding.btnAddFriend.visibility = View.GONE
                            binding.btnDeleteFriend.visibility = View.VISIBLE
                            binding.btnRequestFriend.visibility = View.GONE
                            binding.btnGoChatting.visibility = View.VISIBLE
                        }
                        "Pending" -> {
                            binding.btnAddFriend.visibility = View.GONE
                            binding.btnDeleteFriend.visibility = View.GONE
                            binding.btnRequestFriend.visibility = View.VISIBLE
                            binding.btnGoChatting.visibility = View.GONE
                        }
                        "No" -> {
                            binding.btnAddFriend.visibility = View.VISIBLE
                            binding.btnDeleteFriend.visibility = View.GONE
                            binding.btnRequestFriend.visibility = View.GONE
                            binding.btnGoChatting.visibility = View.GONE
                        }
                    }


                } else {
                    Log.d("UserData Get", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FriendResponse>, t: Throwable) {
                Log.d("UserData Get", "get failed.")
            }
        })
    }

    fun friendAdd() {
        val requestBody =
            "{\"friendNickname\":\"$nickname\"}".toRequestBody("application/json".toMediaTypeOrNull())

        val friendaddCall = RetrofitAPI.friendService
        friendaddCall.friendAdd(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    binding.btnAddFriend.visibility = View.GONE
                    binding.btnDeleteFriend.visibility = View.GONE
                    binding.btnRequestFriend.visibility = View.VISIBLE
                    binding.btnGoChatting.visibility = View.GONE
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
