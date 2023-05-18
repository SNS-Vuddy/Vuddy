package com.b305.vuddy.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.model.App
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentChatBinding
import com.b305.vuddy.model.Chat
import com.b305.vuddy.util.adapter.ChatAdapter
import com.b305.vuddy.util.ChatSocket
import com.google.android.material.textfield.TextInputEditText

class ChatFragment: Fragment() {

    lateinit var binding: FragmentChatBinding
    private val chatList = App.instance.getChatList()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputChatText: TextInputEditText
    private lateinit var chatSocket: ChatSocket
    private var chat: Chat? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)

        inputChatText = binding.inputChatText
        binding.sendBtn.setOnClickListener {
            val chatText = inputChatText.text.toString()
            if (chatText.isNotEmpty()) {
                chat?.let { it.chatId?.let { it1 -> chatSocket.sendMessage(it1, chatText) } }
                Toast.makeText(context, chatText, Toast.LENGTH_SHORT).show()
            }
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        arguments?.let {
            binding.userName.text = it.getString("nickname", "")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.chat_list)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        chatAdapter = ChatAdapter(chatList)
        recyclerView.adapter = chatAdapter
    }
}
