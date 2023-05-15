package com.b305.vuddy.fragment

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
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
import com.b305.vuddy.R
import com.b305.vuddy.util.SharedManager
import com.b305.vuddy.databinding.FragmentChatBinding
import com.b305.vuddy.model.Chat
import com.b305.vuddy.util.ChatAdapter
import com.google.android.material.textfield.TextInputEditText
import java.util.Date

@Suppress("DEPRECATION")
class ChatFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentChatBinding
    private val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }
//    private val chatList = sharedManager.getChatList() as ArrayList<Chat>
//    private val myNick = sharedManager.getCurrentUser().nickname
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
                sendMessage(chatText)
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
//        chatAdapter = ChatAdapter(chatList)
        chatAdapter = ChatAdapter(testLsit)
        recyclerView.adapter = chatAdapter
    }


    @SuppressLint("SimpleDateFormat")
    private fun sendMessage(chatText: String) {
        val now = System.currentTimeMillis()
        val date = Date(now)
        //나중에 바꿔줄것 밑의 yyyy-MM-dd는 그냥 20xx년 xx월 xx일만 나오게 하는 식
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val getTime = sdf.format(date)

//        val nickname= myNick
//        val chatId = sharedManager.getChatList()[0].chatId
        val type = "CHAT"

        //example에는 원래는 이미지 url이 들어가야할 자리
//        val item = Chat(preferences.getString("name",""),chating_Text.text.toString(),"example", getTime)
//        chatAdapter.addItem(item)
//        chatAdapter.notifyDataSetChanged()

        //채팅 입력창 초기화
        inputChatText.setText("")
    }
}
