package com.b305.vuddy.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.App
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentChatBinding
import com.b305.vuddy.model.Chat
import com.b305.vuddy.util.ChatAdapter
import com.b305.vuddy.util.ChatSocket
import com.google.android.material.textfield.TextInputEditText

@Suppress("DEPRECATION")
class ChatFragment: Fragment() {
    private lateinit var chatSocket: ChatSocket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentChatBinding
    private val chatList = App.instance.getChatList()
    private val myNick = App.instance.getCurrentUser().nickname
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputChatText: TextInputEditText
    private var chat: Chat? = null

    val testLsit = arrayListOf(
        Chat("https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg",1, "busbus", "test", "2023-05-12"),
        Chat("https://pbs.twimg.com/media/FSDI5HBaQAA1ATl.jpg", 1, "김채원", "배고파", "2023-05-12"),
        Chat("https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", 1, "busbus", "나도 배고파", "2023-05-12"),
        Chat("https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", 1, "busbus", "오점무", "2023-05-12"),
        Chat("https://pbs.twimg.com/media/FSDI5HBaQAA1ATl.jpg", 1, "김채원", "몰루", "2023-05-12"),
        Chat("https://pbs.twimg.com/media/FSDI5HBaQAA1ATl.jpg", 1, "김채원", "배고파서 뭐든 맛있을듯ㅋㅋ", "2023-05-12"),
        Chat("https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", 1, "busbus", "ㄹㅇㅋㅋ", "2023-05-12"),
        Chat("https://pbs.twimg.com/media/FSDI5HBaQAA1ATl.jpg", 1, "김채원", "빨리 점심시간 돼라", "2023-05-12"),
        Chat("https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", 1, "busbus", "그러게", "2023-05-12"),
        Chat("https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", 1, "busbus", "오늘 시간 느리네", "2023-05-12"),
    )

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

        arguments?.let { bundle ->
            chat = bundle.getParcelable("chat")
            Log.d("Chat", "!!!!!ChatFragment $chat")
            binding.userName.text = chat?.nickname
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
//        chatAdapter = ChatAdapter(testLsit)
        recyclerView.adapter = chatAdapter
    }
}
