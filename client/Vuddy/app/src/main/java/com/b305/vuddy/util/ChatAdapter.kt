package com.b305.vuddy.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.App
import com.b305.vuddy.R
import com.b305.vuddy.model.Chat
import com.bumptech.glide.Glide

class ChatAdapter(private val chatLsit: ArrayList<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val defaultProfile = R.drawable.man
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if(viewType == 1){
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_chat, parent, false)
            return Holder(view)
        }
        //getItemViewType 에서 뷰타입 2을 리턴받았다면 상대채팅레이아웃을 받은 Holder2를 리턴
        else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_your_chat, parent, false)
            return Holder2(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = chatLsit[position]
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder라면 내채팅, item_my_chat의 뷰들을 초기화 해줌
        if (holder is Holder) {
            (holder as Holder).chatText?.setText(chatLsit[position].message)
            (holder as Holder).chatTime?.setText(chatLsit[position].time)
        }
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else if(holder is Holder2) {
            Glide.with(holder.itemView)
                .load(currentItem.profileImage) // 불러올 이미지 url
                .placeholder(defaultProfile) // 이미지 로딩 시작하기 전 표시할 이미지
                .error(defaultProfile) // 로딩 에러 발생 시 표시할 이미지
                .fallback(defaultProfile) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                .circleCrop() // 동그랗게 자르기
                .into((holder as Holder2).profileImage) // 이미지를 넣을 뷰
            (holder as Holder2).nickname?.setText(chatLsit[position].nickname)
            (holder as Holder2).chatText?.setText(chatLsit[position].message)
            (holder as Holder2).chatTime?.setText(chatLsit[position].time)
        }
    }

    override fun getItemCount(): Int {
        return chatLsit.size
    }

    //내 채팅 뷰홀더
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //친구목록 모델의 변수들 정의하는부분
        val chatText = itemView.findViewById<TextView>(R.id.chat_text)
        val chatTime = itemView.findViewById<TextView>(R.id.last_time)
    }

    //상대가친 채팅 뷰홀더
    class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //친구목록 모델의 변수들 정의하는부분
        val profileImage = itemView.findViewById<ImageView>(R.id.chat_you_image)
        val nickname = itemView.findViewById<TextView>(R.id.chat_you_name)
        val chatText = itemView.findViewById<TextView>(R.id.chat_text)
        val chatTime = itemView.findViewById<TextView>(R.id.chat_time)
    }

    override fun getItemViewType(position: Int): Int {//여기서 뷰타입을 1, 2로 바꿔서 지정해줘야 내채팅 너채팅을 바꾸면서 쌓을 수 있음
        val userNick = App.instance.getCurrentUser().nickname

        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if (chatLsit[position].nickname == userNick) {
            1
        } else {
            2
        }
    }
}


