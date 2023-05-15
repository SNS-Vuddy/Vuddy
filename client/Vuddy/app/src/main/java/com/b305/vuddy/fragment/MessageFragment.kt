package com.b305.vuddy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.App
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentMessageBinding
import com.b305.vuddy.model.Chat
import com.b305.vuddy.util.ChatRoomAdapter

class MessageFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentMessageBinding
    private val chatRoomList: ArrayList<Chat> = App.instance.getChatRoomList()
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentMessageBinding.inflate(layoutInflater, container, false)

        binding.ivMap.setOnClickListener {
            it.findNavController().navigate(R.id.action_messageFragment_to_mapFragment)
        }
        binding.ivWrite.setOnClickListener {
            val bottomSheetFragment = WriteFeedFragment()
            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
        }
        binding.ivFriend.setOnClickListener {
            it.findNavController().navigate(R.id.action_messageFragment_to_friendFragment)
        }
        binding.ivProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_messageFragment_to_profileFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.chat_room_list)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        chatRoomAdapter = ChatRoomAdapter(chatRoomList, this)
        recyclerView.adapter = chatRoomAdapter
    }
}
