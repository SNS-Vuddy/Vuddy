//package com.b305.vuddy.view.fragment
//
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.b305.vuddy.model.App
//import com.b305.vuddy.R
//import com.b305.vuddy.databinding.FragmentChatBinding
//import com.b305.vuddy.util.adapter.ChatAdapter
//import com.b305.vuddy.view.activity.MainActivity
//import com.google.android.material.textfield.TextInputEditText
//
//@Suppress("DEPRECATION")
//class ChatFragment: Fragment() {
//
//    lateinit var binding: FragmentChatBinding
//    private lateinit var chatAdapter: ChatAdapter
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var inputChatText: TextInputEditText
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
//
//        inputChatText = binding.inputChatText
//
//        binding.backBtn.setOnClickListener {
//            requireActivity().onBackPressed()
//        }
//
//        arguments?.let {
//            binding.userName.text = it.getString("nickname", "")
//        }
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val service = (requireContext() as MainActivity).getServiceInstance()
//        val chatSocket = service?.getChatSocket()
//        val chatList = App.instance.getChatList()
//        val chatId = App.instance.getChatId()
//
//        val layoutManager = LinearLayoutManager(context)
//        recyclerView = view.findViewById(R.id.chat_list)
//        recyclerView.layoutManager = layoutManager
//        //TODO
////        recyclerView.setHasFixedSize(true)
//        recyclerView.setHasFixedSize(false)
//        chatAdapter = ChatAdapter(chatList)
//        recyclerView.adapter = chatAdapter
//
//        binding.sendBtn.setOnClickListener {
//            Log.d("chatting", "!!!!!send $chatId")
//            val chatText = inputChatText.text.toString()
//            Log.d("chatting", "!!!!!send $chatText")
//            if (chatText.isNotEmpty()) {
//                Log.d("chatting", "!!!!!send")
//                chatSocket?.sendMessage(chatText)
//            }
//        }
//    }
//}

package com.b305.vuddy.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.model.App
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentChatBinding
import com.b305.vuddy.model.Chat
import com.b305.vuddy.util.adapter.ChatAdapter
import com.b305.vuddy.view.activity.MainActivity
import com.google.android.material.textfield.TextInputEditText
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Suppress("DEPRECATION")
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputChatText: TextInputEditText
    private lateinit var chatList: MutableList<Chat>
    private lateinit var originalChatList: MutableList<Chat>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)

        inputChatText = binding.inputChatText

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        arguments?.let {
            binding.userName.text = it.getString("nickname", "")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        chatList = App.instance.getChatList().toMutableList()

        originalChatList = App.instance.getChatList().toMutableList()
        chatList = ArrayList(originalChatList)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.chat_list)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(false)
        chatAdapter = ChatAdapter(chatList as ArrayList<Chat>)
        recyclerView.adapter = chatAdapter

        binding.sendBtn.setOnClickListener {
            val chatText = inputChatText.text.toString()
            if (chatText.isNotEmpty()) {
                val chatSocket = (requireContext() as MainActivity).getServiceInstance()?.getChatSocket()
                chatSocket?.sendMessage(chatText)
            }
            inputChatText.text = null
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChatReceived(chat: Chat) {
        chatList.add(chat)
        updateChatList(chatList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateChatList(newChatList: List<Chat>) {
        chatList.clear()
        chatList.addAll(newChatList)
        chatAdapter.notifyDataSetChanged()
    }
}
