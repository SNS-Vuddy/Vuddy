package com.b305.vuddy.util.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.view.fragment.ChatFragment
import com.b305.vuddy.model.Chat
import com.b305.vuddy.view.activity.MainActivity
import com.bumptech.glide.Glide

class ChatRoomAdapter(private val chatRoomList: ArrayList<Chat>) : RecyclerView.Adapter<ChatRoomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room_list, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = bindingAdapterPosition
                val chatRoom : Chat = chatRoomList[curPos]

                val context = itemView.context
                if (context is MainActivity) {
//                    val service = context.getServiceInstance()
//                    val chatSocket = service?.getChatSocket()
//                    chatSocket?.goChatting(chatRoom.nickname)

                    val bundle = Bundle().apply {
                        putString("nickname", chatRoom.nickname)
                    }
                    val chatFragment = ChatFragment()
                    chatFragment.arguments = bundle

                    context.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, chatFragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Log.e("ChatRoomAdapter", "Parent activity is not MainActivity")
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = chatRoomList[position]
        val defaultProfile = R.drawable.bird
        Glide.with(holder.itemView)
            .load(currentItem.profileImage) // 불러올 이미지 url
            .placeholder(defaultProfile) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultProfile) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultProfile) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .circleCrop() // 동그랗게 자르기
            .into(holder.profileImage) // 이미지를 넣을 뷰
        holder.nickname.text = currentItem.nickname
        if (currentItem.message == "null") {
            holder.lastChat.text = ""
        } else {
            holder.lastChat.text = currentItem.message
        }
//        holder.lastTime.text = currentItem.time?.substring(0, 10)
        holder.lastTime.text = currentItem.time
    }

    override fun getItemCount(): Int {
        return chatRoomList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChatRooms(updatedList: ArrayList<Chat>) {
        chatRoomList.clear()
        chatRoomList.addAll(updatedList)
        notifyDataSetChanged()
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.friend_profile)
        val nickname: TextView = itemView.findViewById(R.id.friend_name)
        val lastChat: TextView = itemView.findViewById(R.id.last_chat)
        val lastTime: TextView = itemView.findViewById(R.id.last_time)
    }
}
