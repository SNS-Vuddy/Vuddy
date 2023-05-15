package com.b305.vuddy.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentFriendProfileBinding
import com.b305.vuddy.model.Feed
import com.b305.vuddy.model.FeedDetailViewModel
import com.b305.vuddy.model.FeedResponse
import com.b305.vuddy.model.Feeds
import com.b305.vuddy.model.FeedsResponse
import com.b305.vuddy.model.FriendResponse
import com.b305.vuddy.model.FriendViewModel
import com.b305.vuddy.model.UserResponse
import com.b305.vuddy.util.CommentAdapter
import com.b305.vuddy.util.FeedMineAdapter
import com.b305.vuddy.util.RetrofitAPI
import com.b305.vuddy.util.feedDetailImageAdapter
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FriendProfileFragment : Fragment(){

    private var nickname: String = ""
    lateinit var binding : FragmentFriendProfileBinding

    private lateinit var feedMineAdapter: FeedMineAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: FriendViewModel
    private lateinit var myAdapter: CommentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFriendProfileBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this).get(FriendViewModel::class.java)
        viewModel.friendData.observe(viewLifecycleOwner) { result ->
            result?.let {
                updateUI(it)
                // 클래스 전역 변수에 데이터를 할당
            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()

        }
        binding.btnDeleteFriend.setOnClickListener {
            friendDelete()
        }
//        val navController = Navigation.findNavController(binding.root)
//
        binding.ivMap.setOnClickListener {
            val fragmentB = MapFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragmentB).commit()
//            it.findNavController().navigate(R.id.action_friendProfileFragment_to_mapFragment)
        }

//    binding.ivMessage.setOnClickListener {
//        val fragmentB = MessageFragment()
//        val fragmentManager = requireActivity().supportFragmentManager
//        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragmentB).commit()
//    }
//    binding.ivProfile.setOnClickListener {
//        val fragmentB = ProfileFragment()
//        val fragmentManager = requireActivity().supportFragmentManager
//        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragmentB).commit()
//    }
//    binding.ivFriend.setOnClickListener {
//        val fragmentB = FriendFragment()
//        val fragmentManager = requireActivity().supportFragmentManager
//        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragmentB).commit()
//    }
        binding.ivWrite.setOnClickListener {
            val bottomSheetFragment = WriteFeedFragment()
            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
        }


        binding.btnAddFriend.setOnClickListener {
            friendAdd()
        }
        return binding.root
    }

    private fun updateUI(data: FriendResponse) {
        if (data?.data?.isFriend == "Pending") {
            binding.btnRequestFriend.visibility = View.VISIBLE
            binding.btnAddFriend.visibility = GONE
            binding.btnDeleteFriend.visibility = GONE
            binding.btnDeleteFriend.visibility = GONE
        } else if (data?.data?.isFriend == "Yes") {
            binding.btnDeleteFriend.visibility = View.VISIBLE
            binding.btnAddFriend.visibility = GONE
            binding.btnRequestFriend.visibility = GONE
            binding.messageFriendBtn.visibility = View.VISIBLE
        } else if (data?.data?.isFriend == "No") {
            binding.btnAddFriend.visibility = View.VISIBLE
            binding.btnRequestFriend.visibility = GONE
            binding.btnDeleteFriend.visibility = GONE
            binding.btnDeleteFriend.visibility = GONE

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            nickname = it.getString("nickname", "")
        }

        viewModel.friendData.observe(viewLifecycleOwner) { result ->
            result?.let {
                updateUI(it)
            }
        }
        viewModel.loadFriendData(nickname)

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
        usercall.FriendDataGet(nickname).enqueue(object : Callback<FriendResponse> {
            override fun onResponse(call: Call<FriendResponse>, response: Response<FriendResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("UserData Get", "get successfully. Response: $result")
                    val userNick = binding.friendProfileNickname
                    userNick.text = result?.data?.nickname

                    if (result?.data?.isFriend == "Pending") {
                        binding.btnRequestFriend.visibility = View.VISIBLE
                        binding.btnAddFriend.visibility = GONE
                        binding.btnDeleteFriend.visibility = GONE
                        binding.messageFriendBtn.visibility = GONE
                    } else if (result?.data?.isFriend == "Yes") {
                        binding.btnDeleteFriend.visibility = View.VISIBLE
                        binding.btnAddFriend.visibility = GONE
                        binding.btnRequestFriend.visibility = GONE
                        binding.messageFriendBtn.visibility = View.VISIBLE
                    } else if (result?.data?.isFriend == "No") {
                        binding.btnAddFriend.visibility = View.VISIBLE
                        binding.btnRequestFriend.visibility = GONE
                        binding.btnDeleteFriend.visibility = GONE
                        binding.messageFriendBtn.visibility = GONE

                    }

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

                    viewModel.loadFriendData(nickname)
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
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), "{\"friendNickname\":\"$nickname\"}")

        var friendaddCall = RetrofitAPI.friendService
        friendaddCall.friendAdd(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("친구추가 성공", "get successfully. Response: $result")
                    viewModel.loadFriendData(nickname)
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
    fun friendDelete() {
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), "{\"friendNickname\":\"$nickname\"}")

        var friendaddCall = RetrofitAPI.friendService
        friendaddCall.friendDelete(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("친구삭제 성공", "get successfully. Response: $result")
                    viewModel.loadFriendData(nickname)
                } else {
                    Log.d("친구삭제 실패", "get failed. Response: ${response.message()}")

                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("친구삭제 실패", "get failed")
            }
        }

        )
    }

}
