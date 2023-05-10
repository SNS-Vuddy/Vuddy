package com.b305.vuddy.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.util.ProfileAdapter
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentFriendBinding
import com.b305.vuddy.model.FriendProfile
import com.b305.vuddy.model.FriendsResponse
import com.b305.vuddy.util.RetrofitAPI
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendFragment : Fragment() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentFriendBinding
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var recyclerView: RecyclerView
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFriendBinding.inflate(layoutInflater, container, false)

//        binding.searchBox.setOnClickListener {
//            it.findNavController().navigate(R.id.action_friendFragment_to_searchFragment)
//        }
        binding.ivMap.setOnClickListener {
            it.findNavController().navigate(R.id.action_friendFragment_to_mapFragment)
        }
        binding.ivWrite.setOnClickListener {
            val bottomSheetFragment = WriteFeedFragment()
            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
        }
        binding.ivMessage.setOnClickListener {
            it.findNavController().navigate(R.id.action_friendFragment_to_messageFragment)
        }
        binding.ivProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_friendFragment_to_profileFragment)
        }

        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val service = RetrofitAPI.friendService

        service.getFriendList().enqueue(object : Callback<FriendsResponse> {
            override fun onResponse(call: Call<FriendsResponse>, response: Response<FriendsResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show()
                    val result = response.body()
                    val friendList: ArrayList<FriendProfile> = result?.friendList!!
                    val layoutManager = LinearLayoutManager(context)
                    recyclerView = view.findViewById(R.id.rv_profile)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.setHasFixedSize(true)
                    profileAdapter = ProfileAdapter(friendList)
                    recyclerView.adapter = profileAdapter
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
