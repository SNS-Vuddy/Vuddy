package com.b305.buddy.frament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.buddy.util.ProfileAdapter
import com.b305.buddy.R
import com.b305.buddy.model.Profiles

class FriendFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var recyclerView: RecyclerView

    private val profileList: ArrayList<Profiles> = arrayListOf(
        Profiles(R.drawable.man, "user1", "status1"),
        Profiles(R.drawable.woman, "user2", "status2"),
        Profiles(R.drawable.man, "user3", "status3"),
        Profiles(R.drawable.woman, "user4", "status4"),
        Profiles(R.drawable.man, "user5", "status5"),
        Profiles(R.drawable.woman, "user6", "status6"),
        Profiles(R.drawable.man, "user7", "status7"),
        Profiles(R.drawable.woman, "user8", "status8"),
        Profiles(R.drawable.man, "user9", "status9"),
        Profiles(R.drawable.woman, "user10", "status10"),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        
        val view = inflater.inflate(R.layout.fragment_friend, container, false)
        
        view.findViewById<ImageView>(R.id.iv_map).setOnClickListener {
            it.findNavController().navigate(R.id.action_friendFragment_to_mapFragment)
        }
        
        view.findViewById<ImageView>(R.id.iv_message).setOnClickListener {
            it.findNavController().navigate(R.id.action_friendFragment_to_messageFragment)
        }
        
        view.findViewById<ImageView>(R.id.iv_profile).setOnClickListener {
            it.findNavController().navigate(R.id.action_friendFragment_to_profileFragment)
        }
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.rv_profile)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        profileAdapter = ProfileAdapter(profileList)
        recyclerView.adapter = profileAdapter
    }
}