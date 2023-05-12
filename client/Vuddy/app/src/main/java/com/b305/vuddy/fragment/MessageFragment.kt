package com.b305.vuddy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentMessageBinding
import com.b305.vuddy.model.ChatRoom
import com.b305.vuddy.util.MessageAdapter

class MessageFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentMessageBinding
//    private val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }
//    val chatRoomList: ArrayList<ChatRoom> = sharedManager.getChatRoomList() as ArrayList<ChatRoom>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView

    val testLsit = arrayListOf(
        ChatRoom("", 1, "User1", "test1", "어제"),
        ChatRoom("", 2, "User2", "test2", "어제"),
        ChatRoom("", 3, "User3", "test3", "어제"),
        ChatRoom("", 4, "User4", "test4", "어제"),
        ChatRoom("", 5, "User5", "test5", "어제"),
        ChatRoom("", 6, "User6", "test6", "어제"),
        ChatRoom("", 7, "User7", "test7", "어제"),
        ChatRoom("", 8, "User8", "test8", "어제"),
        ChatRoom("", 9, "User9", "test9", "어제"),
        ChatRoom("", 10, "User10", "test10", "어제"),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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
        messageAdapter = MessageAdapter(testLsit)
        recyclerView.adapter = messageAdapter
    }
}
