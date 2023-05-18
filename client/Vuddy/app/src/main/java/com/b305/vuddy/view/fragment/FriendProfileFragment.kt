package com.b305.vuddy.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentFriendProfileBinding
import com.b305.vuddy.model.App
import com.b305.vuddy.model.Chat
import com.b305.vuddy.model.Feeds
import com.b305.vuddy.model.FeedsResponse
import com.b305.vuddy.model.FriendResponse
import com.b305.vuddy.viewmodel.FriendViewModel
import com.b305.vuddy.util.adapter.FeedMineAdapter
import com.b305.vuddy.service.RetrofitAPI
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.WebSocket
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class FriendProfileFragment : Fragment() {

    private var nickname: String = ""
    lateinit var binding: FragmentFriendProfileBinding

    private lateinit var feedMineAdapter: FeedMineAdapter
    private lateinit var recyclerView: RecyclerView
//    private lateinit var chatSocket: ChatSocket
    private lateinit var webSocket: WebSocket

    private lateinit var viewModel: FriendViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentFriendProfileBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this)[FriendViewModel::class.java]
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
        binding.ivMessage.setOnClickListener {
            val fragmentB = MessageFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragmentB).commit()
        }
        binding.ivProfile.setOnClickListener {
            val fragmentB = ProfileFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragmentB).commit()
        }
        binding.ivFriend.setOnClickListener {
            val fragmentB = FriendFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragmentB).commit()
        }
        binding.ivWrite.setOnClickListener {
            val bottomSheetFragment = WriteFeedFragment()
            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
        }

        binding.btnAddFriend.setOnClickListener {
            friendAdd()
        }
        binding.btnDeleteFriend.setOnClickListener {
            friendDelete()
        }
        binding.btnGoChatting.setOnClickListener {
            Log.d("chatSocket", "!!!!!!chat open1 $nickname")
//            chatSocket.goChatting(nickname)
            goChatting(nickname)
            Log.d("chatSocket", "!!!!!!chat open2")
            val fragmentB = ChatFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragmentB).commit()
        }
//        binding.ivMap.setOnClickListener {
//            it.findNavController().navigate(R.id.action_friendProfileFragment_to_mapFragment)
//        }
//        binding.ivFriend.setOnClickListener {
//            it.findNavController().navigate(R.id.action_friendProfileFragment_to_friendFragment)
//        }
//        binding.ivWrite.setOnClickListener {
//            val bottomSheetFragment = WriteFeedFragment()
//            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
//        }
//        binding.ivMessage.setOnClickListener {
//            it.findNavController().navigate(R.id.action_friendProfileFragment_to_messageFragment)
//        }
//        binding.ivProfile.setOnClickListener {
//            it.findNavController().navigate(R.id.action_friendProfileFragment_to_profileFragment)
//        }

        return binding.root
    }

    private fun updateUI(data: FriendResponse) {
        when (data.data.isFriend) {
            "REQUESTED" -> {
                binding.btnAddFriend.visibility = GONE
                binding.btnAcceptFriend.visibility = GONE
                binding.btnRequestFriend.visibility = View.VISIBLE
                binding.btnDeleteFriend.visibility = GONE
                binding.btnGoChatting.visibility = GONE
            }
            "YES" -> {
                binding.btnAddFriend.visibility = GONE
                binding.btnAcceptFriend.visibility = GONE
                binding.btnRequestFriend.visibility = GONE
                binding.btnDeleteFriend.visibility = View.VISIBLE
                binding.btnGoChatting.visibility = View.VISIBLE
            }
            "NO" -> {
                binding.btnAddFriend.visibility = View.VISIBLE
                binding.btnAcceptFriend.visibility = GONE
                binding.btnRequestFriend.visibility = GONE
                binding.btnDeleteFriend.visibility = GONE
                binding.btnGoChatting.visibility = GONE
            }
            "RECEIVED" -> {
                binding.btnAddFriend.visibility = GONE
                binding.btnAcceptFriend.visibility = View.VISIBLE
                binding.btnRequestFriend.visibility = GONE
                binding.btnDeleteFriend.visibility = GONE
                binding.btnGoChatting.visibility = GONE
            }
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

        call.friendfeedGet(nickname).enqueue(object : Callback<FeedsResponse> {
            override fun onResponse(call: Call<FeedsResponse>, response: Response<FeedsResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("GET All", "get successfully. Response: $result")
                    val feedList: ArrayList<Feeds> = result?.FeedList!!
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
        val userCall = RetrofitAPI.userService
        userCall.friendDataGet(nickname).enqueue(object : Callback<FriendResponse> {
            override fun onResponse(call: Call<FriendResponse>, response: Response<FriendResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("UserData Get", "get successfully. Response: $result")
                    val userNick = binding.friendProfileNickname
                    userNick.text = result?.data?.nickname

                    when (result?.data?.isFriend) {
                        "REQUESTED" -> {
                            binding.btnAddFriend.visibility = GONE
                            binding.btnAcceptFriend.visibility = GONE
                            binding.btnRequestFriend.visibility = View.VISIBLE
                            binding.btnDeleteFriend.visibility = GONE
                            binding.btnGoChatting.visibility = GONE
                        }
                        "YES" -> {
                            binding.btnAddFriend.visibility = GONE
                            binding.btnAcceptFriend.visibility = GONE
                            binding.btnRequestFriend.visibility = GONE
                            binding.btnDeleteFriend.visibility = View.VISIBLE
                            binding.btnGoChatting.visibility = View.VISIBLE
                        }
                        "NO" -> {
                            binding.btnAddFriend.visibility = View.VISIBLE
                            binding.btnAcceptFriend.visibility = GONE
                            binding.btnRequestFriend.visibility = GONE
                            binding.btnDeleteFriend.visibility = GONE
                            binding.btnGoChatting.visibility = GONE
                        }
                        "RECEIVED" -> {
                            binding.btnAddFriend.visibility = GONE
                            binding.btnAcceptFriend.visibility = View.VISIBLE
                            binding.btnRequestFriend.visibility = GONE
                            binding.btnDeleteFriend.visibility = GONE
                            binding.btnGoChatting.visibility = GONE
                        }
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
                        profileImageUrl.setImageResource(R.drawable.bird)
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

    private fun friendAdd() {
        val requestBody =
            "{\"friendNickname\":\"$nickname\"}".toRequestBody("application/json".toMediaTypeOrNull())

        val friendaddCall = RetrofitAPI.friendService
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
        })
    }

    private fun friendDelete() {
        val requestBody =
            "{\"friendNickname\":\"$nickname\"}".toRequestBody("application/json".toMediaTypeOrNull())

        val friendaddCall = RetrofitAPI.friendService
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
        })
    }

    private fun goChatting(nickname: String?) {
        Log.d("chatSocket", "!!!!!!chat open3")
        val chatRoomList: ArrayList<Chat> = App.instance.getChatRoomList()
        Log.d("chatSocket", "!!!!!!chat open5")
        if (chatRoomList.any { it.nickname == nickname }) {
            Log.d("chatSocket", "!!!!!!chat open6")
            val jsonObject = JSONObject()
                .put("nickname1", App.instance.getCurrentUser().nickname)
                .put("nickname2", nickname)
                .put("type", "JOIN")

            webSocket.send(jsonObject.toString())
            Log.d("ChatSocket", "****sendMessage JOIN!!!!!!****")
        } else {
            Log.d("chatSocket", "!!!!!!chat open7")
            val jsonObject = JSONObject()
                .put("nickname1", App.instance.getCurrentUser().nickname)
                .put("nickname2", nickname)
                .put("type", "OPEN")

            webSocket.send(jsonObject.toString())
            Log.d("ChatSocket", "****sendMessage OPEN!!!!!!****")
        }
    }
}
