package com.b305.vuddy.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentProfileBinding
import com.b305.vuddy.model.Feed
import com.b305.vuddy.model.Feeds
import com.b305.vuddy.model.FeedsResponse
import com.b305.vuddy.model.UserResponse
import com.b305.vuddy.util.FeedMineAdapter
import com.b305.vuddy.util.RetrofitAPI
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var feedMineAdapter: FeedMineAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

//        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        binding.ivMap.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_mapFragment)
        }

        binding.ivFriend.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_friendFragment)
        }

        binding.ivMessage.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_messageFragment)
        }

        binding.ivWrite.setOnClickListener {
            val bottomSheetFragment = WriteFeedFragment()
            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
        }

//        binding.profileImage.setOnClickListener {
//            val feedDetailBottom = FeedDetailFragment()
//            feedDetailBottom.show(parentFragmentManager, "bottomSheetTag")
//        }

        binding.profileAlarmBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_alarmFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val call = RetrofitAPI.feedService
//        call.feedMineGet().enqueue(object : Callback<FeedsResponse> {
//            override fun onResponse(call: Call<FeedsResponse>, response: Response<FeedsResponse>) {
//                if (response.isSuccessful) {
//                    val result = response.body()
//                    Log.d("GET All", "get successfully. Response: $result")
//                    val feedList : ArrayList<Feeds> = result?.FeedList!!
//
//                    // 리사이클러뷰
////                    val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//                    //격자 레이아웃
//                    val layoutManager = GridLayoutManager(context, 3)
//
//                    recyclerView = binding.feedsMineList
//                    recyclerView.layoutManager = layoutManager
//
//                    feedMineAdapter = FeedMineAdapter(feedList)
//                    recyclerView.adapter = feedMineAdapter
//                } else {
//                    Log.d("GET All", "get failed. Response: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<FeedsResponse>, t: Throwable) {
//                Log.e("GET All", "get failed.", t)
//            }
//        })
        val usercall = RetrofitAPI.userService
        usercall.userDataGet().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("UserData Get", "get successfully. Response: $result")

                    // 피드
                    val feedList : ArrayList<Feed> = result?.data?.feeds as ArrayList<Feed>
                    val layoutManager = GridLayoutManager(context, 3)

                    recyclerView = binding.feedsMineList
                    recyclerView.layoutManager = layoutManager

                    feedMineAdapter = FeedMineAdapter(feedList)
                    recyclerView.adapter = feedMineAdapter

                    // 닉네임
                    val userNick = binding.profileUser
                    userNick.text = result.data.nickname

                    // 프로필 이미지
                    val profileImageUrl = binding.profileImage
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

