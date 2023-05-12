package com.b305.vuddy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.util.SharedManager
import com.b305.vuddy.databinding.ActivityChatRoomBinding
import com.b305.vuddy.model.Chat
import com.b305.vuddy.model.ChatRoom
import com.b305.vuddy.util.ChatRoomAdapter
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date

class ChatRoomActivity: AppCompatActivity() {
    lateinit var binding: ActivityChatRoomBinding
    val sharedManager: SharedManager by lazy { SharedManager(this) }
    val chatList = sharedManager.getChatList()
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputChatText: TextInputEditText

    val testLsit = arrayListOf(
        Chat(1, "buddy", "test", "https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", "2023-05-12"),
        Chat(1, "김채원", "배고파", "https://pbs.twimg.com/media/FSDI5HBaQAA1ATl.jpg", "2023-05-12"),
        Chat(1, "buddy", "나도 배고파", "https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", "2023-05-12"),
        Chat(1, "buddy", "오점무", "https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", "2023-05-12"),
        Chat(1, "김채원", "몰루", "https://pbs.twimg.com/media/FSDI5HBaQAA1ATl.jpg", "2023-05-12"),
        Chat(1, "김채원", "배고파서 뭐든 맛있을듯ㅋㅋ", "https://pbs.twimg.com/media/FSDI5HBaQAA1ATl.jpg", "2023-05-12"),
        Chat(1, "buddy", "ㄹㅇㅋㅋ", "https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", "2023-05-12"),
        Chat(1, "김채원", "빨리 점심시간 돼라", "https://pbs.twimg.com/media/FSDI5HBaQAA1ATl.jpg", "2023-05-12"),
        Chat(1, "buddy", "그러게", "https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", "2023-05-12"),
        Chat(1, "buddy", "오늘 시간 느리네", "https://img.freepik.com/premium-vector/cute-dinosaur-illustration-dinosaur-kawaii-chibi-vector-drawing-style-dinosaur-cartoon_622550-20.jpg", "2023-05-12"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)

        val layoutManager = LinearLayoutManager(this)
        recyclerView = binding.chatList
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
//        chatRoomAdapter = ChatRoomAdapter(chatList as ArrayList<Chat>)
        chatRoomAdapter = ChatRoomAdapter(testLsit)
        recyclerView.adapter = chatRoomAdapter

        inputChatText = binding.inputChatText
        binding.sendBtn.setOnClickListener {
            val chatText = inputChatText.text.toString()
            if (chatText.length > 0) {
                sendMessage(chatText)
            }
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_chatRoomActivity_to_messageFragment)
        }
    }


    fun sendMessage(chatText: String) {
        val now = System.currentTimeMillis()
        val date = Date(now)
        //나중에 바꿔줄것 밑의 yyyy-MM-dd는 그냥 20xx년 xx월 xx일만 나오게 하는 식
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val getTime = sdf.format(date)

        val nickname= sharedManager.getCurrentUser().nickname
        val chatId = sharedManager.getChatList()[0].chatId
        val type = "CHAT"

        //example에는 원래는 이미지 url이 들어가야할 자리
//        val item = Chat(preferences.getString("name",""),chating_Text.text.toString(),"example", getTime)
//        chatAdapter.addItem(item)
//        chatAdapter.notifyDataSetChanged()

        //채팅 입력창 초기화
        inputChatText.setText("")
    }
}
