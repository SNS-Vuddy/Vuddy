package com.b305.vuddy.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.model.App
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentMessageBinding
import com.b305.vuddy.model.Chat
import com.b305.vuddy.util.adapter.ChatRoomAdapter

class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMessageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupListeners()
        refreshChatRoomList()
    }

    private fun setupViews() {
        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.chatRoomList
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(false)
        chatRoomAdapter = ChatRoomAdapter(ArrayList())
        recyclerView.adapter = chatRoomAdapter
    }

    private fun setupListeners() {
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
    }

    private fun refreshChatRoomList() {
        val chatRoomList: ArrayList<Chat> = App.instance.getChatRoomList()
        chatRoomAdapter.updateChatRooms(chatRoomList)
    }
}
