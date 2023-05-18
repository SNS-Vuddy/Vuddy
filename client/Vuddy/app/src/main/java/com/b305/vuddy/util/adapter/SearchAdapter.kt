package com.b305.vuddy.util.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.view.fragment.FriendProfileFragment
import com.b305.vuddy.model.FriendProfile
import com.bumptech.glide.Glide

class SearchAdapter(private val profileLsit: ArrayList<FriendProfile>) : RecyclerView.Adapter<SearchAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend_list, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = profileLsit[position]
        val defaultProfile = R.drawable.bird
        Glide.with(holder.itemView)
            .load(currentItem.profileImage) // 불러올 이미지 url
            .placeholder(defaultProfile) // 이미지 로딩 시작하기 전 표시할 이미지
            .error(defaultProfile) // 로딩 에러 발생 시 표시할 이미지
            .fallback(defaultProfile) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
            .circleCrop() // 동그랗게 자르기
            .into(holder.profileImage) // 이미지를 넣을 뷰
        holder.nickname.text = currentItem.nickname

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("nickname", currentItem.nickname)
            val friendprofileFragment = FriendProfileFragment()
            friendprofileFragment.arguments = bundle

            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, friendprofileFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return profileLsit.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.friend_profile)
        val nickname: TextView = itemView.findViewById(R.id.friend_name)
    }
}
